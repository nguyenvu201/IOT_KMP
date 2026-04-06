package caonguyen.vu.bluetooth

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Một implementation mẫu (Mock/Fake) dùng để test luồng Bluetooth quét được
 * hoặc chạy trên Simulator chưa có module Bluetooth thực tế.
 * 
 * @param dispatcher CoroutineDispatcher cho background work. Tuỳ chỉnh trong Unit Test để có thể điều khiển thời gian.
 */
class FakeBluetoothScanner(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : BluetoothScanner {
    private val _isScanning = MutableStateFlow(false)
    override val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _discoveredDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    override val discoveredDevices: StateFlow<List<BluetoothDevice>> = _discoveredDevices.asStateFlow()

    private var scanJob: Job? = null
    // Sử dụng dispatcher được inject để dễ dàng Unit test thời gian (advanceTimeBy)
    private val scope = CoroutineScope(dispatcher)

    override fun startScan() {
        if (_isScanning.value) return
        
        _isScanning.value = true
        _discoveredDevices.value = emptyList()

        scanJob = scope.launch {
            var idCount = 1
            while (isActive) {
                delay(500) // Giả lập quét tốn 0.5s mỗi nhịp
                val mockDevice = BluetoothDevice(
                    name = "KMP_IoT_Device_$idCount",
                    macAddress = "AA:BB:CC:DD:EE:${idCount.toString().padStart(2, '0')}",
                    rssi = (-50..-30).random()
                )
                
                val updatedList = _discoveredDevices.value.toMutableList()
                updatedList.add(mockDevice)
                _discoveredDevices.value = updatedList
                
                idCount++
            }
        }
    }

    override fun stopScan() {
        scanJob?.cancel()
        scanJob = null
        _isScanning.value = false
    }
}
