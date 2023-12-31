package hotkitchen.db

import hotkitchen.db.tables.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

object DatabaseFactory {
    fun init() {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/db"
        val database = Database.connect(jdbcURL, driverClassName)

        transaction(database) {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(
                UsersTable,
                CategoryTable,
                MealTable,
                MealsCategories,
                OrderTable,
                MealsOrders
            )
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

