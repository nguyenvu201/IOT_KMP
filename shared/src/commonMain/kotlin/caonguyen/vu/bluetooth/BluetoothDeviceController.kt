package caonguyen.vu.bluetooth

import kotlinx.coroutines.flow.StateFlow

data class BluetoothMessage(
    val id: String,
    val text: String,
    val isFromMe: Boolean,
    val timestamp: Long
)

enum class ConnectionState {
    DISCONNECTED, CONNECTING, CONNECTED
}

/**
 * Điều khiển kết nối 1-1 với một thiết bị sau khi quét thành công.
 * Hỗ trợ các chuẩn Giao tiếp P2P an toàn bộ nhớ (StateFlow).
 */
interface BluetoothDeviceController {
    val connectionState: StateFlow<ConnectionState>
    val messages: StateFlow<List<BluetoothMessage>>
    
    fun connect(macAddress: String)
    fun disconnect()
    fun sendMessage(text: String)
}
