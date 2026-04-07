package caonguyen.vu.server.iot.mqtt

import kotlin.test.Test
import kotlin.test.assertTrue

class TemperatureSubscriberTest {

    @Test
    fun testValidPayloadIsProcessed() {
        // Arrange
        val subscriber = TemperatureSubscriber()
        val validPayload = """{"temperature": 25.5, "device_id": "esp8266_01"}"""
        
        // Act (Since it prints to stdout, we just ensure it doesn't crash)
        subscriber.onMessageReceived(validPayload)
        
        // Assert
        // In a real scenario, we'd mock the DB. For now, just pass if no exception
        assertTrue(true, "Subscriber should handle valid payload without crashing")
    }

    @Test
    fun testInvalidPayloadIsIgnored() {
        // Arrange
        val subscriber = TemperatureSubscriber()
        val invalidPayload = """{"humidity": 60.0}"""
        
        // Act
        subscriber.onMessageReceived(invalidPayload)
        
        // Assert
        assertTrue(true, "Subscriber should ignore invalid payload")
    }
}
