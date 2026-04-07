package caonguyen.vu.ui.dashboard

import caonguyen.vu.shared.models.SensorData
// import dev.icerock.moko.mvvm.viewmodel.ViewModel
// Moko or Voyager simple ViewModel logic

class TemperatureViewModel {
    fun loadLatestTemperature(): SensorData {
        // Logic to fetch from repository
        return SensorData("esp8266_01", 26.5, 45.0, 16100000)
    }
}
