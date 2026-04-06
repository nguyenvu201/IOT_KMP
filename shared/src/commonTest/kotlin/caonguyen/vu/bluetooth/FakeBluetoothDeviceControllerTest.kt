package caonguyen.vu.bluetooth

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class FakeBluetoothDeviceControllerTest {

    private lateinit var controller: FakeBluetoothDeviceController

    @AfterTest
    fun tearDown() {
        if (::controller.isInitialized) {
            controller.disconnect()
        }
    }

    @Test
    fun testConnectAndWelcomeMessage() = runTest {
        controller = FakeBluetoothDeviceController(StandardTestDispatcher(testScheduler))
        
        assertEquals(ConnectionState.DISCONNECTED, controller.connectionState.value)
        
        controller.connect("AA:BB:CC")
        assertEquals(ConnectionState.CONNECTING, controller.connectionState.value)
        
        advanceTimeBy(1501)
        assertEquals(ConnectionState.CONNECTED, controller.connectionState.value)
        assertEquals(1, controller.messages.value.size)
        assertFalse(controller.messages.value.first().isFromMe, "Tin nhắn đầu không phải do người dùng gửi")
        
        controller.disconnect()
    }

    @Test
    fun testSendMessageAndAutoReply() = runTest {
        controller = FakeBluetoothDeviceController(StandardTestDispatcher(testScheduler))
        controller.connect("AA:BB:CC")
        advanceTimeBy(1501) // connect success
        
        controller.sendMessage("Turn On Light")
        assertEquals(2, controller.messages.value.size) // welcome + user msg
        assertTrue(controller.messages.value.last().isFromMe)
        
        advanceTimeBy(1001) // Auto-reply wait
        assertEquals(3, controller.messages.value.size) // welcome + user msg + auto reply
        assertFalse(controller.messages.value.last().isFromMe)
        assertTrue(controller.messages.value.last().text.contains("Turn On Light"))
        
        controller.disconnect()
    }
}
