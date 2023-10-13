package hotkitchen.db

import hotkitchen.models.UserType
import org.jetbrains.exposed.dao.id.IntIdTable

object UsersTable : IntIdTable() {
    val email = varchar("email", 100)
    val userType = enumerationByName<UserType>("userType", 100).default(UserType.OTHER)
    val password = varchar("password", 100)
    val name = varchar("name", 100).default("")
    val phone = varchar("phone", 100).default("")
    val address = varchar("address", 100).default("")

    init {
        index(true, email)
    }
}

