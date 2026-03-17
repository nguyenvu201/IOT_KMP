package caonguyen.vu.server.mqtt

import caonguyen.vu.shared.models.SensorData
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.channels.BufferOverflow
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
            .topicFilter("iot/esp8266/+/status")
            .callback { publish ->
                try {
                    val payload = String(publish.payloadAsBytes)
                    val state = Json.decodeFromString<caonguyen.vu.shared.models.EspPinState>(payload)
                    _espStatusFlow.tryEmit(state)
                } catch(e: Exception) {
                    println("Failed to decode ESP status payload: ${e.message}")
                }
            }
            .send()
            .whenComplete { _, exception ->
                if (exception != null) {
                    println("Failed to subscribe to ESP status: ${exception.message}")
                } else {
                    println("MQTT Gateway: Subscribed to iot/esp8266/+/status")
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
        val topic = "iot/esp8266/node-1/cmd"
        
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
