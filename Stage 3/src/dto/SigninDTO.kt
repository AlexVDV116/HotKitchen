package hotkitchen.dto

import kotlinx.serialization.Serializable

@Serializable
data class SigninDTO (
    val email: String,
    val password: String
)