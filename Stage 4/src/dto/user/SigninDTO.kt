package hotkitchen.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class SigninDTO (
    val email: String,
    val password: String
)