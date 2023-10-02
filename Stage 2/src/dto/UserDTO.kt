package hotkitchen.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO (
    val email: String,
    val password: String
) {
    fun hasValidCredentials(): String {
        return  if (!email.matches(EMAIL_REGEX.toRegex()) || !password.matches(PASSWORD_REGEX.toRegex())) {
            "Invalid email or password"
        } else "Ok"
    }
}