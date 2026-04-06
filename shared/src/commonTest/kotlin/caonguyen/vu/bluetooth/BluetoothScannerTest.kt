package caonguyen.vu.bluetooth

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class BluetoothScannerTest {

    @Test
    fun test1_initialStateIsScanningFalse() = runTest {
        val scanner = FakeBluetoothScanner(StandardTestDispatcher(testScheduler))
        assertFalse(scanner.isScanning.value, "Scanner không nên ở trạng thái quét lúc khởi tạo.")
    }

    @Test
    fun test2_initialDiscoveredDevicesEmpty() = runTest {
        val scanner = FakeBluetoothScanner(StandardTestDispatcher(testScheduler))
        assertTrue(scanner.discoveredDevices.value.isEmpty(), "Danh sách thiết bị phải rỗng ban đầu.")
    }

    @Test
    fun test3_startScanChangesIsScanningToTrue() = runTest {
        val scanner = FakeBluetoothScanner(StandardTestDispatcher(testScheduler))
        scanner.startScan()
        assertTrue(scanner.isScanning.value, "isScanning phải là true sau khi gọi startScan.")
        scanner.stopScan() // Bắt buộc stop để thoát runTest
    }

    @Test
    fun test4_stopScanChangesIsScanningToFalse() = runTest {
        val scanner = FakeBluetoothScanner(StandardTestDispatcher(testScheduler))
        scanner.startScan()
        scanner.stopScan()
        assertFalse(scanner.isScanning.value, "isScanning phải là false sau khi stopScan.")
    }

    @Test
    fun test5_startScanMultipleTimesDoesNotCrash() = runTest {
        val scanner = FakeBluetoothScanner(StandardTestDispatcher(testScheduler))
        scanner.startScan()
        scanner.startScan()
        assertTrue(scanner.isScanning.value, "Trạng thái giữ nguyên true khi start nhiều đợt.")
        scanner.stopScan()
    }

    @Test
    fun test6_stopScanWhenNotScanningDoesNotCrash() = runTest {
        val scanner = FakeBluetoothScanner(StandardTestDispatcher(testScheduler))
        scanner.stopScan()
        assertFalse(scanner.isScanning.value)
    }

    @Test
    fun test7_devicesEmittedPeriodically() = runTest {
        val scanner = FakeBluetoothScanner(StandardTestDispatcher(testScheduler))
        scanner.startScan()
        
        advanceTimeBy(501)
        assertEquals(1, scanner.discoveredDevices.value.size, "Phải tìm được 1 device sau 500ms")
        
        advanceTimeBy(1000)
        assertEquals(3, scanner.discoveredDevices.value.size, "Phải tìm được 3 devices sau 1.5s")
        scanner.stopScan()
    }

    @Test
    fun test8_stopScanHaltsEmission() = runTest {
        val scanner = FakeBluetoothScanner(StandardTestDispatcher(testScheduler))
        scanner.startScan()
        
        advanceTimeBy(501)
        assertEquals(1, scanner.discoveredDevices.value.size)
        
        scanner.stopScan() // Ngừng quét thực sự
        advanceTimeBy(2000)
        assertEquals(1, scanner.discoveredDevices.value.size, "List device không được tăng thêm sau stopScan")
    }

    @Test
    fun test9_startScanClearsPreviousHistory() = runTest {
        val scanner = FakeBluetoothScanner(StandardTestDispatcher(testScheduler))
        scanner.startScan()
        advanceTimeBy(1001)
        assertEquals(2, scanner.discoveredDevices.value.size)
        
        scanner.stopScan()
        
        scanner.startScan()
        assertEquals(0, scanner.discoveredDevices.value.size, "List phải bị reset về rỗng ở đợt quét mới")
        scanner.stopScan()
    }

    @Test
    fun test10_deviceFormatMatchesIoTNeeds() = runTest {
        val scanner = FakeBluetoothScanner(StandardTestDispatcher(testScheduler))
        scanner.startScan()
        advanceTimeBy(501)
        
        val firstDevice = scanner.discoveredDevices.value.first()
        assertTrue(firstDevice.name?.startsWith("KMP_IoT_Device_") == true, "Tên fake device sai chuẩn")
        assertTrue(firstDevice.macAddress.length == 17, "Độ dài MAC addresses chuẩn là 17 kí tự")
        assertTrue(firstDevice.rssi in -100..0, "RSSI phải là số âm hoặc 0")
        scanner.stopScan()
    }
}
