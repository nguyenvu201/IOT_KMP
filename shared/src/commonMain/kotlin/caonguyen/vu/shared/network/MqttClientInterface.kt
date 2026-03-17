package caonguyen.vu.shared.network

import caonguyen.vu.shared.models.SensorData
import kotlinx.coroutines.flow.Flow

interface MqttClientInterface {
    suspend fun connect(brokerUrl: String, clientId: String): Result<Unit>
    suspend fun disconnect(): Result<Unit>
    suspend fun subscribeToTopic(topic: String): Result<Unit>
    suspend fun publish(topic: String, payload: String): Result<Unit>
    
    val sensorDataStream: Flow<SensorData>
    val connectionState: Flow<MqttConnectionState>
}

enum class MqttConnectionState {
    CONNECTED, DISCONNECTED, CONNECTING, ERROR
}
