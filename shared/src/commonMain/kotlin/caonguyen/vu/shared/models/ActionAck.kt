package caonguyen.vu.shared.models

import kotlinx.serialization.Serializable

@Serializable
data class ActionAck(
    val actionType: String,
    val target: String,
    val isSuccess: Boolean,
    val message: String? = null
)
