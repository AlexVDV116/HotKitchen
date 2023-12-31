package hotkitchen.error

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException

open class EntityNotFound(entity: String) : Exception("""{"status":"$entity not found"}""")
open class InvalidFieldError(field: String) : Exception("""{"status":"Invalid $field"}""")
class InvalidEmailError : Exception("""{"status":"Invalid email"}""")
class InvalidPasswordError : Exception("""{"status":"Invalid password"}""")
class AuthenticationFailed : Exception("""{"status":"Authentication Failed"}""")
class EmailAlreadyRegistered : Exception("""{"status":"Email already registered"}""")
class EmailUpdateAttemptError : Exception("""{"status":"Email update not allowed"}""")
class WrongIdFormatException : Exception("""{"status":"Wrong ID format"}""")
class WrongIdRangeException : Exception("""{"status":"Wrong ID range"}""")
class WrongEmailFormat : Exception("""{"status":"Wrong email format"}""")
class WrongPasswordFormat : Exception("""{"status":"Wrong password format"}""")
class BearerStrangeError : Exception("""{"Authorization":"Bearer something.very.strange"}""")
class WrongUserType : Exception("""{"status":"Access denied"}""")
class MissingPermissionError(requiredUserType: String) : Exception("""{"status":"User is not of userType: $requiredUserType"}""")

@OptIn(ExperimentalSerializationApi::class)
fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        exception<InvalidEmailError> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        exception<InvalidPasswordError> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        exception<AuthenticationFailed> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        exception<EmailAlreadyRegistered> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        exception<EmailUpdateAttemptError> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        exception<WrongIdFormatException> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        exception<WrongIdRangeException> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        exception<WrongPasswordFormat> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        exception<BearerStrangeError> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Unauthorized)
        }
        exception<WrongUserType> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        exception<WrongUserType> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        exception<MissingPermissionError> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Unauthorized)
        }
        exception<EntityNotFound> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.BadRequest)
        }
        exception<MissingFieldException> { call, cause ->
            call.respondText(
                cause.message ?: "Some fields are missing", ContentType.Application.Json, HttpStatusCode.Forbidden
            )
        }
    }
}
