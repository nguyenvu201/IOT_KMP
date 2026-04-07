package caonguyen.vu.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import caonguyen.vu.shared.models.DeviceConfig
import caonguyen.vu.shared.models.DeviceStatus
import caonguyen.vu.shared.models.SensorData
import caonguyen.vu.ui.IoTPrimary
import caonguyen.vu.ui.IoTSecondary
import caonguyen.vu.ui.IoTSurface
import org.koin.compose.koinInject

class DashboardScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinInject<DashboardViewModel>()
        val devices by viewModel.devices.collectAsState()
        val sensorDataMap by viewModel.sensorDataMap.collectAsState()

        val navigator = cafe.adriel.voyager.navigator.LocalNavigator.current
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(Unit) {
            viewModel.actionAcks.collect { ack ->
                if (!ack.isSuccess) {
                    val icon = "❌"
                    snackbarHostState.showSnackbar(
                        message = "$icon ${ack.message ?: "Action failed for ${ack.target}"}",
                        withDismissAction = true
                    )
                }
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
            ) {
                val isMobile = maxWidth < 800.dp
                
                if (isMobile) {
                    // Mobile Layout: Single Column, Scrollable
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        HeaderSection(navigator)
                        
                        Text(
                            "RS485 Network",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 0.dp)
                        )
                        // Using a simple Column loop instead of LazyGrid to allow scrolling safely in a scrollable parent
                        devices.filter { it.deviceId.startsWith("MOCK") || it.deviceId.startsWith("NODE") }.forEach { device ->
                            DeviceCard(device, sensorDataMap[device.deviceId])
                        }
                        
                        Text(
                            "NodeMCU Direct Control",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 0.dp)
                        )
                        EspControlCard(viewModel, isMobile = true)
                    }
                } else {
                    // Desktop Layout: Two Columns, Grid System
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        HeaderSection(navigator)
                        
                        Row(modifier = Modifier.fillMaxSize().weight(1f), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                            // Left Column: RS485 Devices
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "RS485 Network",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                LazyVerticalGrid(
                                    columns = GridCells.Adaptive(300.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    items(devices.filter { it.deviceId.startsWith("MOCK") || it.deviceId.startsWith("NODE") }) { device ->
                                        DeviceCard(device, sensorDataMap[device.deviceId])
                                    }
                                }
                            }
                            
                            // Right Column: NodeMCU Direct Control
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "NodeMCU Direct Control",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                EspControlCard(viewModel, isMobile = false)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun HeaderSection(navigator: cafe.adriel.voyager.navigator.Navigator?) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Column {
                Text(
                    "IoT Ecosystem",
                    color = IoTPrimary,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    "Live System Overview",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 0.dp)
                )
            }
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Button(
                    onClick = { navigator?.push(caonguyen.vu.ui.bluetooth.BluetoothScannerScreen()) },
                    colors = ButtonDefaults.buttonColors(containerColor = IoTPrimary)
                ) {
                    Text("Scan Bluetooth", color = androidx.compose.ui.graphics.Color.Black, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { 
                        navigator?.push(TemperatureScreen(null)) 
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IoTSecondary)
                ) {
                    Text("View Temperature", color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { 
                        navigator?.push(MonitoringScreen()) 
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IoTSecondary)
                ) {
                    Text("Live Charts", color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    @Composable
    fun EspControlCard(viewModel: DashboardViewModel, isMobile: Boolean) {
        val espPins by viewModel.espPins.collectAsState()
        
        val modifier = if (isMobile) {
            Modifier.fillMaxWidth().wrapContentHeight()
        } else {
            Modifier.fillMaxWidth().fillMaxHeight()
        }
        
        Card(
            colors = CardDefaults.cardColors(containerColor = IoTSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = modifier
        ) {
            Column(modifier = Modifier.padding(24.dp).fillMaxSize()) {
                Text("ESP8266 GPIO Map", color = IoTPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                // Analog Slider
                val analogPin = espPins.find { it.isAnalog }
                if (analogPin != null) {
                    Text("Analog Input (${analogPin.pin})", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${analogPin.value.toInt()} / 1024", color = IoTPrimary, fontSize = 24.sp, fontWeight = FontWeight.Light)
                    Slider(
                        value = analogPin.value.toFloat(),
                        onValueChange = { viewModel.setAnalogPin(analogPin.pin, it.toDouble(), publishToMqtt = false) },
                        onValueChangeFinished = { viewModel.setAnalogPin(analogPin.pin, analogPin.value, publishToMqtt = true) },
                        valueRange = 0f..1024f,
                        colors = SliderDefaults.colors(thumbColor = IoTPrimary, activeTrackColor = IoTPrimary)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
                
                Text("Digital Outputs", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(8.dp))
                
                // Digital Switches
                val cols = if (isMobile) 2 else 3
                
                if (isMobile) {
                    // Mobile: manual row layout to avoid internal lazy crashes inside vertical scroll
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        val chunkedPins = espPins.filter { !it.isAnalog }.chunked(cols)
                        chunkedPins.forEach { rowPins ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                rowPins.forEach { pin ->
                                    PinSwitchCard(pin, viewModel, Modifier.weight(1f))
                                }
                                val emptyCols = cols - rowPins.size
                                repeat(emptyCols) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(cols),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(espPins.filter { !it.isAnalog }) { pin ->
                            PinSwitchCard(pin, viewModel, Modifier)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PinSwitchCard(pin: caonguyen.vu.shared.models.EspPinState, viewModel: DashboardViewModel, modifier: Modifier = Modifier) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            shape = RoundedCornerShape(8.dp),
            modifier = modifier
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(pin.pin, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                Switch(
                    checked = pin.value > 0.5,
                    onCheckedChange = { viewModel.toggleDigitalPin(pin.pin, it) },
                    colors = SwitchDefaults.colors(checkedThumbColor = IoTPrimary, checkedTrackColor = IoTPrimary.copy(alpha = 0.5f))
                )
            }
        }
    }

    @Composable
    fun DeviceCard(device: DeviceConfig, data: SensorData?) {
        val statusColor = when(device.status) {
            DeviceStatus.ONLINE -> IoTPrimary
            DeviceStatus.ERROR -> IoTSecondary
            DeviceStatus.OFFLINE -> MaterialTheme.colorScheme.onSurfaceVariant
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = IoTSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(140.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(device.name, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                    Box(modifier = Modifier.size(12.dp).background(statusColor, RoundedCornerShape(6.dp)))
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                if (data != null) {
                    Text(
                        text = "${data.temperature.toString().take(5)} °C",
                        color = IoTPrimary,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Light
                    )
                } else {
                    Text(
                        "No Data",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 24.sp
                    )
                }
                Text("Type: ${device.type}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            }
        }
    }
}
