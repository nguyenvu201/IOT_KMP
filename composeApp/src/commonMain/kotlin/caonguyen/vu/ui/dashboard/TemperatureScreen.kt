package caonguyen.vu.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import caonguyen.vu.shared.models.SensorData
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class TemperatureScreen(val data: SensorData?) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        
        val displayData = data ?: SensorData(
            deviceId = "MOCK-TEMP-DEVICE",
            temperature = kotlin.random.Random.nextDouble(20.0, 30.0),
            humidity = kotlin.random.Random.nextDouble(40.0, 70.0),
            timestamp = 0L
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                TextButton(onClick = { navigator.pop() }) {
                    Text("< Back", color = androidx.compose.ui.graphics.Color.Blue)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Temperature Dashboard", style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            if (data == null) {
                Text("MODE: MOCK DATA (Testing UI)", color = androidx.compose.ui.graphics.Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            Text("Device: ${displayData.deviceId}")
            Text("Temperature: ${((displayData.temperature * 100).toInt() / 100.0)} °C")
            Text("Humidity: ${((displayData.humidity * 100).toInt() / 100.0)} %")
        }
    }
}
