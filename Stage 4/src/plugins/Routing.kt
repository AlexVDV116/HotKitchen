package hotkitchen.plugins

import hotkitchen.dto.user.SignupDTO
import hotkitchen.error.*
import hotkitchen.routes.*
import hotkitchen.routes.category.categoryRoutes
import hotkitchen.routes.meal.mealRoutes
import hotkitchen.routes.user.meRoutes
import hotkitchen.routes.user.signinRoutes
import hotkitchen.routes.user.signupRoutes
import hotkitchen.routes.user.userRoutes
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
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
        exception<UserNotFound> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.NotFound)
        }
        exception<BearerStrangeError> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Unauthorized)
        }
        exception<MealNotFound> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        exception<WrongUserType> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        exception<CategoryNotFound> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
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
        mealRoutes()
        categoryRoutes()
    }
}

