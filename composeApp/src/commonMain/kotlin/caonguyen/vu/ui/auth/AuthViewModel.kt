package caonguyen.vu.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caonguyen.vu.shared.models.LoginRequest
import caonguyen.vu.shared.models.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

object AuthState {
    var token: String? = null
}

class AuthViewModel : ViewModel() {
    private val client = HttpClient()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    
    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess = _loginSuccess.asStateFlow()

    fun login(username: String, pin: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val serverHost = caonguyen.vu.shared.buildconfig.BuildKonfig.WEBSOCKET_HOST
                val response = client.post("http://$serverHost:8085/api/login") {
                    contentType(ContentType.Application.Json)
                    setBody(Json.encodeToString(LoginRequest(username, pin)))
                }
                
                if (response.status.value in 200..299) {
                    val loginResponse = Json.decodeFromString<LoginResponse>(response.bodyAsText())
                    AuthState.token = loginResponse.token
                    _loginSuccess.value = true
                } else {
                    _error.value = "Invalid username or password"
                }
            } catch (e: Exception) {
                _error.value = "Connection error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
