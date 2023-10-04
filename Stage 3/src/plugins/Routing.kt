package hotkitchen.plugins

import hotkitchen.dto.SignupDTO
import hotkitchen.error.*
import hotkitchen.routes.*
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            call.respondText(cause.reasons[0], ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        exception<InvalidEmailError> { call, cause ->
            call.respond(HttpStatusCode.Forbidden, hashMapOf("Authorization" to "Invalid email"))
        }
        exception<InvalidPasswordError> { call, cause ->
            call.respond(HttpStatusCode.Forbidden, hashMapOf("Authorization" to "Invalid password"))
        }
        exception<AuthenticationFailed> { call, cause ->
            call.respond(HttpStatusCode.Forbidden, hashMapOf("Authorization" to "Authentication failed"))
        }
        exception<EmailAlreadyRegistered> { call, cause ->
           call.respond(HttpStatusCode.Forbidden, hashMapOf("Authorization" to "Email is already registered"))
        }
        exception<WrongIdFormatException> { call, cause ->
            call.respond(HttpStatusCode.Forbidden, hashMapOf("Authorization" to "Wrong ID format exception"))
        }
        exception<WrongIdRangeException> { call, cause ->
            call.respond(HttpStatusCode.Forbidden, hashMapOf("Authorization" to "Wrong ID range exception"))
        }
        exception<WrongPasswordFormat> { call, cause ->
            call.respond(HttpStatusCode.Forbidden, hashMapOf("Authorization" to "Wrong password format"))
        }
        exception<UserNotFound> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, hashMapOf("Authorization" to "User not found"))
        }
        exception<BearerStrangeError> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized, hashMapOf("Authorization" to cause))
        }
    }

    install(RequestValidation) {
        validate<SignupDTO> { user ->
            try {
                user.hasValidCredentials()
                ValidationResult.Valid
            } catch (e: Exception) {
                ValidationResult.Invalid(e.message!!)
            }
        }
    }

    routing {
        userRoutes()
        signupRoutes()
        signinRoutes()
        validateTokenRoute()
        meRoutes()
    }
}

