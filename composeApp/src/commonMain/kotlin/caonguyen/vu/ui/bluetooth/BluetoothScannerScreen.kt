package caonguyen.vu.ui.bluetooth

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import caonguyen.vu.bluetooth.BluetoothScanner
import org.koin.compose.koinInject

class BluetoothScannerScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current ?: return
        val scanner = koinInject<BluetoothScanner>()
        
        val isScanning by scanner.isScanning.collectAsState()
        val devices by scanner.discoveredDevices.collectAsState()

        LaunchedEffect(Unit) {
            scanner.startScan()
        }

        DisposableEffect(Unit) {
            onDispose {
                scanner.stopScan()
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Radar Scan", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Text("< Back", color = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF1E1E1E),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            },
            containerColor = Color(0xFF121212)
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Radar Pulse Animation (Aesthetic Glassmorphism style)
                RadarPulse(isScanning)
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = if (isScanning) "Searching for devices..." else "Scan resting",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodyLarge
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(devices, key = { it.macAddress }) { device ->
                        DeviceCard(
                            name = device.name ?: "Unknown Device",
                            mac = device.macAddress,
                            rssi = device.rssi,
                            onClick = {
                                navigator.push(BluetoothChatScreen(device.macAddress, device.name ?: "Unknown Device"))
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun RadarPulse(isScanning: Boolean) {
        val infiniteTransition = rememberInfiniteTransition()
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = if (isScanning) 3f else 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            )
        )
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.5f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        Box(
            modifier = Modifier.size(150.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isScanning) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(scale)
                        .clip(CircleShape)
                        .background(Color(0xFF00C853).copy(alpha = alpha))
                )
            }
            
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00E676)),
                contentAlignment = Alignment.Center
            ) {
                Text("!", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }

    @Composable
    fun DeviceCard(name: String, mac: String, rssi: Int, onClick: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable { onClick() },
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(name, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(mac, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                }
                Text("$rssi dBm", color = if (rssi > -60) Color(0xFF00E676) else Color(0xFFFFB300), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
