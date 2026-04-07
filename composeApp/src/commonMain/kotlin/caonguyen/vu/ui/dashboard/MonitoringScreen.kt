package caonguyen.vu.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import caonguyen.vu.ui.components.SensorBarChart
import caonguyen.vu.ui.components.SensorLineChart
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class MonitoringScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinInject<DashboardViewModel>()
        val history by viewModel.sensorHistory.collectAsState()

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { navigator.pop() }) {
                    Text("< Back", color = Color.Blue)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Monitoring Dashboard", style = MaterialTheme.typography.titleLarge)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (history.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No telemetry data yet...", color = Color.Gray)
                }
                return
            }

            val temps = history.map { it.temperature }
            val hums = history.map { it.humidity }
            val flows = history.map { it.waterFlow }
            val phLevels = history.map { it.phLevel }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    ChartCard("Temperature (°C)", Color.Red) {
                        SensorLineChart(data = temps, lineColor = Color.Red)
                    }
                }
                item {
                    ChartCard("Humidity (%)", Color.Blue) {
                        SensorBarChart(data = hums, barColor = Color.Blue)
                    }
                }
                item {
                    ChartCard("Water Flow (L/min)", Color.Cyan) {
                        SensorLineChart(data = flows, lineColor = Color.Cyan)
                    }
                }
                item {
                    ChartCard("pH Level", Color.Green) {
                        SensorBarChart(data = phLevels, barColor = Color.Green)
                    }
                }
            }
        }
    }

    @Composable
    private fun ChartCard(title: String, titleColor: Color, chartContent: @Composable () -> Unit) {
        Card(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(title, style = MaterialTheme.typography.titleMedium, color = titleColor)
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    chartContent()
                }
            }
        }
    }
}
