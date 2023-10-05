package hotkitchen.repository.user

import hotkitchen.dto.user.*
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
            it.toUserDto()
        }
    }

    override suspend fun getUserById(id: UserId): UserDTO = transaction {
        UserEntity.findById(id)?.toUserDto() ?: throw UserNotFound()
    }

    override suspend fun createUser(user: SignupDTO): UserDTO = transaction {
        UserEntity.new {
            email = user.email
            userType = user.userType
            password = user.hashedPassword()
        }.toUserDto()
    }

    override suspend fun updateUser(id: UserId, user: UpdateUserDTO) = transaction {
        UserEntity.findById(id)?.let {
            it.password = user.hashedPassword()
            it.userType = user.userType
            it.name = user.name
            it.phone = user.phone
            it.address = user.address
        } ?: throw UserNotFound()
    }

    override suspend fun deleteUser(id: UserId) = transaction {
        UserEntity.findById(id)?.delete() ?: throw UserNotFound()
    }

    override suspend fun authenticateUser(user: SigninDTO): UserDTO {
        val userEntity = transaction {
            UserEntity.find { UsersTable.email eq user.email }.firstOrNull() ?: throw AuthenticationFailed()
        }
        if (!BCrypt.checkpw(user.password, userEntity.password)) {
            throw AuthenticationFailed()
        } else {
            return userEntity.toUserDto()
        }
    }
}