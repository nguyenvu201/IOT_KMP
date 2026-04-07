package caonguyen.vu.shared.models

import kotlinx.serialization.Serializable

@Serializable
data class SensorData(
    val deviceId: String,
    val temperature: Double,
    val humidity: Double = 0.0,
    val timestamp: Long
)
