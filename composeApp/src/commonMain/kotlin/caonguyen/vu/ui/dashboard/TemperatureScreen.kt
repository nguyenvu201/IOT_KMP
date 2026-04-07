package caonguyen.vu.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import caonguyen.vu.shared.models.SensorData

@Composable
fun TemperatureScreen(data: SensorData?) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Temperature Dashboard")
        Spacer(modifier = Modifier.height(16.dp))
        
        if (data != null) {
            Text("Device: ${data.deviceId}")
            Text("Temperature: ${data.temperature} °C")
            Text("Humidity: ${data.humidity} %")
        } else {
            Text("Waiting for sensor data...")
        }
    }
}
