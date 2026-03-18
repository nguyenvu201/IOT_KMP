package caonguyen.vu

import caonguyen.vu.server.gateway.RS485Service
import caonguyen.vu.server.mqtt.MqttGateway
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.cors.routing.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.server.request.*
import java.util.Date

fun main() {
    embeddedServer(Netty, port = 8085, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    
    // Initialize Database Connection Pool first
    caonguyen.vu.server.database.DatabaseFactory.init()

    val mqttGateway = MqttGateway()
    val rs485Service = RS485Service()
    
    // Connect to MQTT broker based on config (Docker injects this)
    val mqttHost = System.getenv("MQTT_BROKER_HOST") ?: caonguyen.vu.shared.buildconfig.BuildKonfig.MQTT_BROKER_HOST
    mqttGateway.connect(mqttHost, 1883)
    mqttGateway.subscribeToEspStatus()
    
    // We start listening to the mocked RS485 port.
    // In production, portName would be /dev/ttyUSB0 (Linux) or COM3 (Windows). 
    // Here we wrap in try/catch to avoid crashing on devices without serial ports connected.
    try {
        rs485Service.startListening("COM1") 
    } catch(e: Exception) {
        println("Could not start RS485: ${e.message}")
    }

    // Forward RS485 data to MQTT Gateway asynchronously
    CoroutineScope(Dispatchers.IO).launch {
        // Fallback mocked data generation if no real RS485 port is available for demo purposes
        launch {
            while (true) {
                delay(5000)
                val mockData = caonguyen.vu.shared.models.SensorData(
                    deviceId = "MOCK-RS485-Node-1",
                    timestamp = System.currentTimeMillis(),
                    value = (20..35).random().toDouble() + Math.random(),
                    unit = "°C",
                    sensorType = "Temperature"
                )
                mqttGateway.publishSensorData(mockData)
            }
        }
        
        rs485Service.sensorDataFlow.collect { sensorData ->
            println("Application: Forwarding parsed hardware data to MQTT Broker... $sensorData")
            mqttGateway.publishSensorData(sensorData)
        }
    }

    install(WebSockets) {
        pingPeriodMillis = 15000
        timeoutMillis = 45000
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    install(CORS) {
        anyHost()
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
    }

    val secret = System.getenv("JWT_SECRET") ?: "my-super-secret-iot-key"
    val issuer = System.getenv("JWT_ISSUER") ?: "http://0.0.0.0:8085/"
    val audience = System.getenv("JWT_AUDIENCE") ?: "http://0.0.0.0:8085/"
    val myRealm = "Access to the IoT Gateway"
    
    val adminUser = System.getenv("ADMIN_USERNAME") ?: "admin"
    val adminPass = System.getenv("ADMIN_PASSWORD") ?: "admin123"

    install(Authentication) {
        jwt("auth-jwt") {
            realm = myRealm
            verifier(JWT
                .require(Algorithm.HMAC256(secret))
                .withAudience(audience)
                .withIssuer(issuer)
                .build())
            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else null
            }
            authHeader { call ->
                try {
                    val header = call.request.parseAuthorizationHeader()
                    if (header != null) return@authHeader header
                } catch (e: Exception) {}
                
                val token = call.request.queryParameters["token"]
                if (token != null) {
                    return@authHeader HttpAuthHeader.Single("Bearer", token)
                }
                null
            }
        }
    }

    routing {
        get("/") {
            call.respondText("IoT Gateway is Running. MQTT Bridge active.")
        }
        
        post("/api/login") {
            try {
                val body = call.receiveText()
                val request = kotlinx.serialization.json.Json.decodeFromString<caonguyen.vu.shared.models.LoginRequest>(body)
                if (request.username == adminUser && request.password == adminPass) {
                    val token = JWT.create()
                        .withAudience(audience)
                        .withIssuer(issuer)
                        .withClaim("username", request.username)
                        .withExpiresAt(Date(System.currentTimeMillis() + 60000 * 60 * 24)) // 1 day
                        .sign(Algorithm.HMAC256(secret))
                    
                    val response = caonguyen.vu.shared.models.LoginResponse(token)
                    call.respondText(
                        text = kotlinx.serialization.json.Json.encodeToString(response),
                        contentType = ContentType.Application.Json
                    )
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid username or password")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid request format")
            }
        }
        
        authenticate("auth-jwt") {
            webSocket("/ws/esp8266") {
                println("App connected via WebSockets!")
                
                val receiveJob = launch {
                    mqttGateway.espStatusFlow.collect { status ->
                        val jsonString = kotlinx.serialization.json.Json.encodeToString(status)
                        send(jsonString)
                    }
                }
                
                try {
                    incoming.consumeEach { frame ->
                        if (frame is Frame.Text) {
                            val payload = frame.readText()
                            println("Server WebSocket received text payload: $payload")
                            try {
                                val state = kotlinx.serialization.json.Json.decodeFromString<caonguyen.vu.shared.models.EspPinState>(payload)
                                println("Server checking MQTT Gateway to publish command for ${state.pin}...")
                                mqttGateway.publishEspCommand(state) { isSuccess, error ->
                                    launch {
                                        val ack = caonguyen.vu.shared.models.ActionAck(
                                            actionType = "MQTT_PUBLISH",
                                            target = state.pin,
                                            isSuccess = isSuccess,
                                            message = if (isSuccess) "Successfully updated ${state.pin}" else error
                                        )
                                        send(kotlinx.serialization.json.Json.encodeToString(ack))
                                    }
                                }
                            } catch(e: Exception) {
                                println("Invalid WS payload: $payload")
                            }
                        }
                    }
                } finally {
                    println("App WebSocket disconnected.")
                    receiveJob.cancel()
                }
            }
        }
    }
}