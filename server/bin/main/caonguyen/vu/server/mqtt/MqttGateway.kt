package caonguyen.vu.server.mqtt

import caonguyen.vu.shared.models.SensorData
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.insert
import java.util.UUID

class MqttGateway {

    private lateinit var client: Mqtt5AsyncClient

    fun connect(brokerHost: String, port: Int = 1883) {
        client = MqttClient.builder()
            .useMqttVersion5()
            .identifier("Ktor-IoT-Gateway-${UUID.randomUUID()}")
            .serverHost(brokerHost)
            .serverPort(port)
            .buildAsync()

        client.connectWith()
            .send()
            .whenComplete { _, throwable ->
                if (throwable != null) {
                    println("MQTT Gateway: Connection failed! ${throwable.message}")
                } else {
                    println("MQTT Gateway: Connected to broker at $brokerHost:$port")
                }
            }
    }

    private val _espStatusFlow = MutableSharedFlow<caonguyen.vu.shared.models.EspPinState>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val espStatusFlow = _espStatusFlow.asSharedFlow()

    fun subscribeToEspStatus() {
        if (!this::client.isInitialized) return
        
        client.subscribeWith()
            .topicFilter(caonguyen.vu.shared.buildconfig.BuildKonfig.MQTT_STATUS_TOPIC_FILTER)
            .callback { publish ->
                try {
                    val payload = String(publish.payloadAsBytes)
                    val state = Json.decodeFromString<caonguyen.vu.shared.models.EspPinState>(payload)
                    _espStatusFlow.tryEmit(state)
                    
                    // LƯU DB: Lưu tự động vào PostgreSQL
                    kotlinx.coroutines.CoroutineScope(Dispatchers.IO).launch {
                        try {
                            caonguyen.vu.server.database.DatabaseFactory.dbQuery {
                                caonguyen.vu.server.database.SensorDataTable.insert {
                                    it[pin] = state.pin
                                    it[isAnalog] = state.isAnalog
                                    it[value] = state.value
                                    it[recordedAt] = kotlinx.datetime.Clock.System.now()
                                }
                            }
                            println("Database: Đã lưu dữ liệu chân ${state.pin} với giá trị ${state.value} xuống PostgreSQL!")
                        } catch (e: Exception) {
                            println("Database Lỗi: Không thể lưu xuống DB - ${e.message}")
                        }
                    }
                } catch(e: Exception) {
                    println("Failed to decode ESP status payload: ${e.message}")
                }
            }
            .send()
            .whenComplete { _, exception ->
                if (exception != null) {
                    println("Failed to subscribe to ESP status: ${exception.message}")
                } else {
                    println("MQTT Gateway: Subscribed to ${caonguyen.vu.shared.buildconfig.BuildKonfig.MQTT_STATUS_TOPIC_FILTER}")
                }
            }
    }

    fun publishSensorData(data: SensorData) {
        if (!this::client.isInitialized) return
        
        val payload = Json.encodeToString(data).toByteArray()
        val topic = "iot/sensors/${data.deviceId}"
        
        client.publishWith()
            .topic(topic)
            .payload(payload)
            .send()
            .whenComplete { _, exception ->
                if (exception != null) {
                    println("Failed to publish SensorData: ${exception.message}")
                }
            }
    }

    fun publishEspCommand(state: caonguyen.vu.shared.models.EspPinState, onComplete: ((Boolean, String?) -> Unit)? = null) {
        if (!this::client.isInitialized) {
            onComplete?.invoke(false, "MQTT Client not initialized")
            return
        }
        
        val payload = Json.encodeToString(state).toByteArray()
        val topic = caonguyen.vu.shared.buildconfig.BuildKonfig.MQTT_CMD_TOPIC
        
        println("MQTT Gateway: Attempting to publish to topic '$topic' payload: ${Json.encodeToString(state)}")
        
        client.publishWith()
            .topic(topic)
            .payload(payload)
            .send()
            .whenComplete { _, exception ->
                if (exception != null) {
                    println("MQTT Gateway: Failed to publish EspPinState: ${exception.message}")
                    onComplete?.invoke(false, exception.message)
                } else {
                    println("MQTT Gateway: Successfully published EspPinState to topic '$topic'")
                    onComplete?.invoke(true, null)
                }
            }
    }

    fun disconnect() {
        if (this::client.isInitialized) {
            client.disconnect()
        }
    }
}
