package hotkitchen.repository.user

import hotkitchen.dto.*
import hotkitchen.models.*


interface UserRepository {
    suspend fun allUsers(): List<UserDTO>
    suspend fun getUserById(id: UserId): UserDTO
    suspend fun createUser(user: SignupDTO): UserEntity
    suspend fun updateUser(id: UserId, user: UpdateUserDTO)
    suspend fun deleteUser(id: UserId)
    suspend fun authenticateUser(user: SigninDTO): UserEntity
}