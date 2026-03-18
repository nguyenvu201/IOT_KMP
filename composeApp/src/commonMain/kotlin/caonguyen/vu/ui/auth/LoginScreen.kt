package caonguyen.vu.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import caonguyen.vu.ui.IoTPrimary
import caonguyen.vu.ui.IoTSurface
import caonguyen.vu.ui.dashboard.DashboardScreen
import org.koin.compose.koinInject

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<AuthViewModel>()
        val navigator = LocalNavigator.currentOrThrow
        
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        
        val isLoading by viewModel.isLoading.collectAsState()
        val error by viewModel.error.collectAsState()
        val loginSuccess by viewModel.loginSuccess.collectAsState()
        
        LaunchedEffect(loginSuccess) {
            if (loginSuccess) {
                navigator.replace(DashboardScreen())
            }
        }
        
        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = IoTSurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.width(400.dp).padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("IoT Admin Login", color = IoTPrimary, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true
                    )
                    
                    if (error != null) {
                        Text(text = error!!, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
                    }
                    
                    Button(
                        onClick = { viewModel.login(username, password) },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        enabled = !isLoading && username.isNotBlank() && password.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = IoTPrimary)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Login", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
