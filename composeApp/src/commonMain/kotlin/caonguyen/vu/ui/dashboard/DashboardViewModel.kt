package caonguyen.vu.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caonguyen.vu.shared.models.DeviceConfig
import caonguyen.vu.shared.models.DeviceStatus
import caonguyen.vu.shared.models.SensorData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DashboardViewModel : ViewModel() {
    
    private val _devices = MutableStateFlow<List<DeviceConfig>>(emptyList())
    val devices = _devices.asStateFlow()
    
    // In a real app, this maps device IDs to real-time streams
    private val _sensorDataMap = MutableStateFlow<Map<String, SensorData>>(emptyMap())
    val sensorDataMap = _sensorDataMap.asStateFlow()

    private val _espPins = MutableStateFlow<List<caonguyen.vu.shared.models.EspPinState>>(emptyList())
    val espPins = _espPins.asStateFlow()
    
    // Lazy HttpClient inside ViewModel for now to avoid restructuring Koin 
    private val client = io.ktor.client.HttpClient {
        install(WebSockets) {
            pingIntervalMillis = 20_000L
        }
    }
    private var wsSession: DefaultClientWebSocketSession? = null

    init {
        loadMockDevices()
        loadMockEspPins()
        simulateLiveMqttData()
        connectWebSockets()
    }
    
    private fun connectWebSockets() {
        viewModelScope.launch {
            try {
                // Change to actual PC IP later for phone testing. Using localhost for desktop test.
                client.webSocket(method = io.ktor.http.HttpMethod.Get, host = "localhost", port = 8085, path = "/ws/esp8266") {
                    wsSession = this
                    println("App WebSocket Connected to Server!")
                    
                    try {
                        incoming.consumeEach { frame ->
                            if (frame is Frame.Text) {
                                val text = frame.readText()
                                try {
                                    val state = kotlinx.serialization.json.Json.decodeFromString<caonguyen.vu.shared.models.EspPinState>(text)
                                    // Update StateFlow
                                    _espPins.value = _espPins.value.map {
                                        if (it.pin == state.pin) state else it
                                    }
                                } catch(e: Exception) {
                                    println("Invalid state payload: $text")
                                }
                            }
                        }
                    } catch(e: Exception) {
                        println("WebSocket Reception Error: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                println("Failed to connect to Websocket Gateway: ${e.message}")
            }
        }
    }

    private fun loadMockEspPins() {
        val pins = mutableListOf<caonguyen.vu.shared.models.EspPinState>()
        for (i in 0..8) {
            pins.add(caonguyen.vu.shared.models.EspPinState("D$i", isAnalog = false, value = 0.0))
        }
        pins.add(caonguyen.vu.shared.models.EspPinState("A0", isAnalog = true, value = 512.0))
        _espPins.value = pins
    }

    fun toggleDigitalPin(pinName: String, newValue: Boolean) {
        println("ViewModel: Toggled $pinName to $newValue")
        val state = caonguyen.vu.shared.models.EspPinState(pinName, isAnalog = false, value = if (newValue) 1.0 else 0.0)
        _espPins.value = _espPins.value.map {
            if (it.pin == pinName) state else it
        }
        
        viewModelScope.launch {
            try {
                wsSession?.send(Frame.Text(Json.encodeToString(state)))
            } catch(e: Exception) {
                println("Failed to publish pin update: ${e.message}")
            }
        }
    }
    
    fun setAnalogPin(pinName: String, newValue: Double) {
        println("ViewModel: Set Analog $pinName to $newValue")
        val state = caonguyen.vu.shared.models.EspPinState(pinName, isAnalog = true, value = newValue)
        _espPins.value = _espPins.value.map {
            if (it.pin == pinName) state else it
        }
        
        viewModelScope.launch {
            try {
                wsSession?.send(Frame.Text(Json.encodeToString(state)))
            } catch(e: Exception) {
                println("Failed to publish analog update: ${e.message}")
            }
        }
    }

    private fun loadMockDevices() {
        val mockedDevices = listOf(
            DeviceConfig("MOCK-RS485-Node-1", "Main Hall Sensor", "Temperature", DeviceStatus.ONLINE),
            DeviceConfig("MOCK-RS485-Node-2", "HVAC Controller", "HVAC", DeviceStatus.ONLINE),
            DeviceConfig("NODE-ERR-3", "Water Leak Detector", "Moisture", DeviceStatus.ERROR),
            DeviceConfig("ESP8266-NODE", "Living Room NodeMCU", "WiFi Controller", DeviceStatus.ONLINE)
        )
        _devices.value = mockedDevices
    }

    private fun simulateLiveMqttData() {
        viewModelScope.launch {
            while (true) {
                delay(2000)
                val newTemp = (20..35).random().toDouble() + kotlin.random.Random.nextDouble()
                val currentMap = _sensorDataMap.value.toMutableMap()
                
                currentMap["MOCK-RS485-Node-1"] = SensorData(
                    deviceId = "MOCK-RS485-Node-1",
                    timestamp = 0L, // Mocked timestamp to avoid adding kotlinx-datetime
                    value = newTemp,
                    unit = "°C",
                    sensorType = "Temperature"
                )
                
                // Randomly fluctuate Analog A0 for visual effect
                _espPins.value = _espPins.value.map {
                    if (it.isAnalog) it.copy(value = (it.value + (-10..10).random()).coerceIn(0.0, 1024.0)) else it
                }
                
                _sensorDataMap.value = currentMap
            }
        }
    }
}
