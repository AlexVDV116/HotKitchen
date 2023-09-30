package hotkitchen.dto

import kotlinx.serialization.Serializable

@Serializable
data class SigninUserDto (val email: String, val password: String)