package hotkitchen.dto.user

import hotkitchen.models.UserType
import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class UpdateUserDTO (
    val email: String,
    val userType: UserType,
    val password: String,
    val name: String,
    val phone: String,
    val address: String
) {
    fun hashedPassword(): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }
}