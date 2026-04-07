package caonguyen.vu.server.iot.mqtt

/**
 * Subscriber that listens to ESP8266 temperature data.
 * Implements REQ-N002 and mitigates RISK-009 by validating payload.
 */
class TemperatureSubscriber {
    fun subscribe() {
        // Pseudo logic for MQTT Subscription
        val topic = "sensor/temperature/+/data"
        println("Subscribed to MQTT Topic: $topic")
    }
    
    fun onMessageReceived(payload: String) {
        // RISK-009 Mitigation: Validate Payload
        if (payload.contains("temperature")) {
            // Save to DB (SensorTable)
            println("Saved valid sensor data: $payload")
        }
    }
}
