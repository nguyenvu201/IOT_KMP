package caonguyen.vu.server.gateway

import caonguyen.vu.shared.models.SensorData
import com.fazecast.jSerialComm.SerialPort
import com.fazecast.jSerialComm.SerialPortDataListener
import com.fazecast.jSerialComm.SerialPortEvent
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class RS485Service {
    
    private var activePort: SerialPort? = null
    
    private val _sensorDataFlow = MutableSharedFlow<SensorData>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sensorDataFlow = _sensorDataFlow.asSharedFlow()

    fun startListening(portName: String, baudRate: Int = 9600) {
        val port = SerialPort.getCommPort(portName)
        port.setBaudRate(baudRate)
        
        if (port.openPort()) {
            activePort = port
            println("RS485Service: Successfully opened port $portName")
            
            port.addDataListener(object : SerialPortDataListener {
                override fun getListeningEvents(): Int { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE }
                
                override fun serialEvent(event: SerialPortEvent?) {
                    if (event?.eventType != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) return
                    val newData = ByteArray(port.bytesAvailable())
                    port.readBytes(newData, newData.size)
                    
                    // Here we mock parsing standard RS485 payload (e.g. Modbus RTU)
                    val rawString = String(newData)
                    println("RS485 Payload: $rawString")
                    
                    // Simulating parsed Modbus data into Domain Model
                    val data = SensorData(
                        deviceId = "RS485-Node-1",
                        timestamp = System.currentTimeMillis(),
                        value = (20..35).random().toDouble() + Math.random(),
                        unit = "°C",
                        sensorType = "Temperature"
                    )
                    
                    _sensorDataFlow.tryEmit(data)
                }
            })
        } else {
            println("RS485Service: Failed to open port $portName")
        }
    }

    fun stopListening() {
        activePort?.closePort()
        activePort = null
    }
}
