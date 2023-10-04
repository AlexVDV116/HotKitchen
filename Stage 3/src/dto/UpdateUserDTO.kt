package hotkitchen.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserDTO (
    val userType: String,
    val name: String,
    val phone: String,
    val address: String
)