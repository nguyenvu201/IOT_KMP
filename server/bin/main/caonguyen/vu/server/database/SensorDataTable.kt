package caonguyen.vu.server.database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object SensorDataTable : IntIdTable("sensor_data") {
    val pin = varchar("pin", 50)
    val isAnalog = bool("is_analog")
    val value = double("value")
    
    // We use kotlinx.datetime.Instant mapped to PostgreSQL Timestamp.
    val recordedAt = timestamp("recorded_at")
}
