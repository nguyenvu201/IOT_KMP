package caonguyen.vu.shared.repository

import caonguyen.vu.shared.models.DeviceConfig
import caonguyen.vu.shared.models.SensorData
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    fun getConnectedDevices(): Flow<List<DeviceConfig>>
    fun getSensorData(deviceId: String): Flow<SensorData>
    
    suspend fun fetchHistoricalData(deviceId: String, startTime: Long, endTime: Long): List<SensorData>
    suspend fun sendCommand(deviceId: String, command: String): Result<Unit>
}
