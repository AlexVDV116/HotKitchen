package hotkitchen.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

@Serializable
data class User(val id: Int, val email: String, val userType: String, val password: String)

object Users : Table() {
    val id = integer("id").autoIncrement()
    val email = varchar("email", 128)
    val userType = varchar("userType", 128)
    val password = varchar("password", 256)

    override val primaryKey = PrimaryKey(id)
}