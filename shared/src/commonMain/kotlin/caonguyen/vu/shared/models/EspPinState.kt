package caonguyen.vu.shared.models

import kotlinx.serialization.Serializable

@Serializable
data class EspPinState(
    val pin: String, // e.g., "D0", "D1", "A0"
    val isAnalog: Boolean,
    val value: Double, // 0.0 or 1.0 for Digital, 0.0-1024.0 for Analog
    val mode: String = "OUTPUT" // "INPUT" or "OUTPUT"
)
