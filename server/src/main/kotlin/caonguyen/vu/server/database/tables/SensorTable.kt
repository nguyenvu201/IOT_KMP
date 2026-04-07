package caonguyen.vu.server.database.tables

import org.jetbrains.exposed.sql.Table

object SensorTable : Table() {
    val id = integer("id").autoIncrement()
    val deviceId = varchar("device_id", 50)
    val temperature = double("temperature")
    val humidity = double("humidity").default(0.0)
    val timestamp = long("timestamp")
    
    override val primaryKey = PrimaryKey(id)
}
