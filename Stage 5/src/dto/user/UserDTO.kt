package hotkitchen.dto.user

import hotkitchen.db.dao.UserType
import kotlinx.serialization.Serializable

typealias UserId = Int

@Serializable
data class UserDTO(
    val id: Int,
    val email: String,
    val userType: UserType,
    val name: String,
    val phone: String,
    val address: String
)