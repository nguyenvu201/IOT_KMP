package caonguyen.vu.bluetooth

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class FakeBluetoothDeviceController(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : BluetoothDeviceController {

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    override val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private val _messages = MutableStateFlow<List<BluetoothMessage>>(emptyList())
    override val messages: StateFlow<List<BluetoothMessage>> = _messages.asStateFlow()

    private val scope = CoroutineScope(dispatcher)
    private var job: Job? = null

    override fun connect(macAddress: String) {
        if (_connectionState.value != ConnectionState.DISCONNECTED) return
        
        _connectionState.value = ConnectionState.CONNECTING
        job = scope.launch {
            delay(1500) // Giả định Mất 1.5s để Handshake Bluetooth
            _connectionState.value = ConnectionState.CONNECTED
            
            // Phát ra lời chào mừng khi join thành công
            val welcomeMessage = BluetoothMessage(
                id = Random.nextLong().toString(),
                text = "Connected to $macAddress. Hardware ready.",
                isFromMe = false,
                timestamp = 0L // Mock
            )
            _messages.value = listOf(welcomeMessage)
        }
    }

    override fun disconnect() {
        job?.cancel()
        job = null
        _connectionState.value = ConnectionState.DISCONNECTED
        _messages.value = emptyList()
    }

    override fun sendMessage(text: String) {
        if (_connectionState.value != ConnectionState.CONNECTED) return
        
        // Push tin nhắn của Mobile User lên list
        val userMsg = BluetoothMessage(
            id = Random.nextLong().toString(),
            text = text,
            isFromMe = true,
            timestamp = 1L
        )
        
        val currentMessages = _messages.value.toMutableList()
        currentMessages.add(userMsg)
        _messages.value = currentMessages

        // Coroutine mô phỏng thiết bị thực tế Auto-reply lại sau 1s xử lý
        scope.launch {
            delay(1000)
            val autoReply = BluetoothMessage(
                id = Random.nextLong().toString(),
                text = "ACK: $text",
                isFromMe = false,
                timestamp = 2L
            )
            val updated = _messages.value.toMutableList()
            updated.add(autoReply)
            _messages.value = updated
        }
    }
}
