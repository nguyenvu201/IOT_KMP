package caonguyen.vu.shared.network

import caonguyen.vu.shared.models.SensorData

interface SensorApi {
    suspend fun getLatestTemperature(): Result<SensorData>
    suspend fun getTemperatureHistory(): Result<List<SensorData>>
}
