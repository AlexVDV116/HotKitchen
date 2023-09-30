package hotkitchen.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserDto (val email: String, val userType: String, val password: String)