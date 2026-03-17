package caonguyen.vu.server.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val driverClassName = "org.postgresql.Driver"
        val jdbcURL = "jdbc:postgresql://127.0.0.1:5432/kmp_iot_db"
        val database = Database.connect(hikari(jdbcURL, driverClassName))
        
        // Create tables automatically
        transaction(database) {
            SchemaUtils.create(SensorDataTable)
        }
    }

    private fun hikari(url: String, driver: String): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = driver
            jdbcUrl = url
            // These should match the docker-compose.yml POSTGRES_USER and POSTGRES_PASSWORD
            username = "kmp_iot_user"
            password = "kmp_iot_password"
            
            // Hikari pool optimizations
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        return HikariDataSource(config)
    }

    // Generic suspended transaction helper to run IO-blocking DB operations 
    // off the main thread/event loop safely.
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
