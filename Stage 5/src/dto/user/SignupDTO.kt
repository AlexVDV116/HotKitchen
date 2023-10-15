package hotkitchen.dto.user

import hotkitchen.error.WrongEmailFormat
import hotkitchen.error.WrongPasswordFormat
import hotkitchen.db.dao.UserType
import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class SignupDTO (
    val email: String,
    val userType: UserType,
    val password: String
) {
    fun hashedPassword(): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun validate() {
        val emailRegex = Regex(pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        val passwordRegex = Regex(pattern = "^(?=.+[A-Za-z])(?=.+\\d)[A-Za-z\\d]{6,}\$")

        if (!email.matches(emailRegex)) throw WrongEmailFormat()
        if (!password.matches(passwordRegex)) throw WrongPasswordFormat()
    }

}
