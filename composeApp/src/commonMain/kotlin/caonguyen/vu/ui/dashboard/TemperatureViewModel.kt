package caonguyen.vu.ui.dashboard

import caonguyen.vu.shared.models.SensorData
// import dev.icerock.moko.mvvm.viewmodel.ViewModel
// Moko or Voyager simple ViewModel logic

class TemperatureViewModel {
    fun loadLatestTemperature(): SensorData {
        // Logic to fetch from repository
        return SensorData(
            deviceId = "esp8266_01", 
            temperature = 26.5, 
            humidity = 45.0, 
            timestamp = 16100000L
        )
    }
}
