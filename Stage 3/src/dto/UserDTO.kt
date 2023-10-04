package hotkitchen.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO (
    val id: Int,
    val email: String,
    val userType: String,
    val name: String,
    val phone: String,
    val address: String
)