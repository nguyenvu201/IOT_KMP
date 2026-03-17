package caonguyen.vu.shared.models

import kotlinx.serialization.Serializable

@Serializable
data class DeviceConfig(
    val deviceId: String,
    val name: String,
    val type: String,
    val status: DeviceStatus
)

@Serializable
enum class DeviceStatus {
    ONLINE, OFFLINE, ERROR
}
