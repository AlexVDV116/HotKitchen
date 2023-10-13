package hotkitchen.models

import hotkitchen.dto.user.UserDTO
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import hotkitchen.db.UsersTable

enum class UserType {
    STAFF, CLIENT, OTHER
}

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UsersTable)

    var email by UsersTable.email
    var userType by UsersTable.userType
    var password by UsersTable.password
    var name by UsersTable.name
    var phone by UsersTable.phone
    var address by UsersTable.address
}

fun UserEntity.toUserDto() = UserDTO(
    this.id.value,
    this.email,
    this.userType,
    this.name,
    this.phone,
    this.address
)