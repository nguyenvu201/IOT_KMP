package caonguyen.vu.shared.models

import kotlinx.serialization.Serializable

@Serializable
data class SensorData(
    val deviceId: String,
    val timestamp: Long,
    val value: Double,
    val unit: String,
    val sensorType: String
)
