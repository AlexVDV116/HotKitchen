package hotkitchen.dao

import hotkitchen.models.*

interface DAOFacade {
    suspend fun allUsers(): List<User>
    suspend fun user(id: Int): User?
    suspend fun addUser(email: String, userType: String, password: String): User?
    suspend fun editUser(id: Int, email: String, userType: String, password: String): Boolean
    suspend fun deleteUser(id: Int): Boolean
    suspend fun signinUser(email: String, password: String): Boolean
}