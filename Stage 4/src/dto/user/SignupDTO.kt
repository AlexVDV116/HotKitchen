package hotkitchen.dto.user

import hotkitchen.error.WrongEmailFormat
import hotkitchen.error.WrongPasswordFormat
import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

/*
Password REGEX enforces these rules:
- At least one upper case English letter, (?=.*?[A-Z])
- At least one lower case English letter, (?=.*?[a-z])
- At least one digit, (?=.*?[0-9])
- At least one special character, (?=.*?[#?!@$%^&*-])
- Minimum eight in length .{8,} (with the anchors)

Email should contain local seperated by an @. i.e. local@domain
 */
const val EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
//const val PASSWORD_REGEX = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$"
const val PASSWORD_REGEX = "^(?=.+[A-Za-z])(?=.+\\d)[A-Za-z\\d]{6,}\$"

@Serializable
data class SignupDTO (
    val email: String,
    val userType: String,
    val password: String
) {
    fun hashedPassword(): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun hasValidCredentials() {
        if (!email.matches(EMAIL_REGEX.toRegex())) {
            throw WrongEmailFormat()
        }
        if (!password.matches(PASSWORD_REGEX.toRegex())) {
            throw WrongPasswordFormat()
        }
    }

}