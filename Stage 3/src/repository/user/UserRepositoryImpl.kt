package hotkitchen.repository.user

import hotkitchen.dto.*
import hotkitchen.error.AuthenticationFailed
import hotkitchen.error.UserNotFound
import hotkitchen.models.UserEntity
import hotkitchen.models.UsersTable
import hotkitchen.models.toUserDto
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

class UserRepositoryImpl : UserRepository {
    override suspend fun allUsers(): List<UserDTO> = transaction {
        UserEntity.all().map {
            UserDTO(
                it.id.value,
                it.email,
                it.userType,
                it.name,
                it.phone,
                it.address
            )
        }
    }

    override suspend fun getUserById(id: UserId): UserDTO = transaction {
        UserEntity.findById(id)?.toUserDto() ?: throw UserNotFound()
    }

    override suspend fun createUser(user: SignupDTO): UserEntity = transaction {
        UserEntity.new {
            email = user.email
            userType = user.userType
            password = user.hashedPassword()
        }
    }

    override suspend fun updateUser(id: UserId, user: UpdateUserDTO): Unit = transaction {
        UserEntity.findById(id)?.let {
            it.userType = user.userType
            it.name = user.name
            it.phone = user.phone
            it.address = user.address
        } ?: throw UserNotFound()
    }

    override suspend fun deleteUser(id: UserId): Unit = transaction {
        UserEntity.findById(id)?.delete() ?: throw UserNotFound()
    }

    override suspend fun authenticateUser(user: SigninDTO): UserEntity {
        val userEntity = transaction {
            UserEntity.find { UsersTable.email eq user.email }.firstOrNull() ?: throw AuthenticationFailed()
        }
        if (!BCrypt.checkpw(user.password, userEntity.password)) {
            throw AuthenticationFailed()
        } else {
            return userEntity
        }
    }
}