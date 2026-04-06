package caonguyen.vu.ui.bluetooth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import caonguyen.vu.bluetooth.BluetoothDeviceController
import caonguyen.vu.bluetooth.ConnectionState
import org.koin.compose.koinInject

class BluetoothChatScreen(private val macAddress: String, private val deviceName: String) : Screen {
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current ?: return
        val controller = koinInject<BluetoothDeviceController>()
        
        val connectionState by controller.connectionState.collectAsState()
        val messages by controller.messages.collectAsState()
        
        var inputText by remember { mutableStateOf("") }

        LaunchedEffect(macAddress) {
            controller.connect(macAddress)
        }

        DisposableEffect(Unit) {
            onDispose {
                controller.disconnect()
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        Column {
                            Text(deviceName, fontWeight = FontWeight.Bold)
                            Text(
                                text = connectionState.name, 
                                style = MaterialTheme.typography.bodySmall,
                                color = when(connectionState) {
                                    ConnectionState.CONNECTED -> Color(0xFF00E676)
                                    ConnectionState.CONNECTING -> Color(0xFFFFB300)
                                    else -> Color.Red
                                }
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Text("Back", color = Color.White)
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
            ) {
                // Hộp thư (Chat history)
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    reverseLayout = false
                ) {
                    items(messages) { msg ->
                        ChatBubble(msg.text, msg.isFromMe)
                    }
                }

                // Input bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFF2C2C2C))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Send IoT command...", color = Color.Gray) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White
                        ),
                        enabled = connectionState == ConnectionState.CONNECTED
                    )
                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank() && connectionState == ConnectionState.CONNECTED) {
                                controller.sendMessage(inputText)
                                inputText = ""
                            }
                        },
                        enabled = connectionState == ConnectionState.CONNECTED
                    ) {
                        Text("Send", color = Color(0xFF00E676), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    @Composable
    fun ChatBubble(text: String, isFromMe: Boolean) {
        val alignment = if (isFromMe) Alignment.End else Alignment.Start
        val bgColor = if (isFromMe) Color(0xFF00C853) else Color(0xFF383838)
        val textColor = Color.White
        
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            horizontalAlignment = alignment
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isFromMe) 16.dp else 4.dp,
                            bottomEnd = if (isFromMe) 4.dp else 16.dp
                        )
                    )
                    .background(bgColor)
                    .padding(12.dp)
            ) {
                Text(text, color = textColor)
            }
        }
    }
}
