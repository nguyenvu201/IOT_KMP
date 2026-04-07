package caonguyen.vu.server.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import caonguyen.vu.shared.models.SensorData
import kotlinx.datetime.Clock

fun Route.sensorRoutes() {
    route("/api/sensors") {
        get("/temperature/latest") {
            // REQ-B007: Return latest temperature (Mocked for integration)
            val mockData = SensorData(
                deviceId = "esp8266_01",
                temperature = 26.5,
                humidity = 45.0,
                timestamp = Clock.System.now().toEpochMilliseconds()
            )
            call.respond(mockData)
        }
        
        get("/temperature/history") {
            // Returns history
            call.respond(emptyList<SensorData>())
        }
    }
}
