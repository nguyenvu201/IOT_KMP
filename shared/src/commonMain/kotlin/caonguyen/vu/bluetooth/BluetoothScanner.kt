package caonguyen.vu.bluetooth

import kotlinx.coroutines.flow.StateFlow

data class BluetoothDevice(
    val name: String?,
    val macAddress: String,
    val rssi: Int
)

/**
 * Interface để quét thiết bị Bluetooth cho nền tảng di động và IoT.
 * Được thiết kế chuẩn xác với Kotlin Multiplatform, sử dụng StateFlow cho
 * việc quản lý bộ nhớ và luồng dữ liệu an toàn.
 */
interface BluetoothScanner {
    /**
     * Trạng thái quét hiện tại.
     */
    val isScanning: StateFlow<Boolean>

    /**
     * Danh sách các thiết bị đã được phát hiện trong quá trình quét.
     */
    val discoveredDevices: StateFlow<List<BluetoothDevice>>

    /**
     * Bắt đầu quét thiết bị Bluetooth.
     */
    fun startScan()

    /**
     * Dừng quét thiết bị Bluetooth để giải phóng resources.
     */
    fun stopScan()
}
