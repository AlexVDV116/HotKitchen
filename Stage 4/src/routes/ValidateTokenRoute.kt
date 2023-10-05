package hotkitchen.routes

import hotkitchen.error.BearerStrangeError
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.validateTokenRoute() {
    authenticate {
        route("validate") {
            get {
                val principal = call.principal<JWTPrincipal>()
                val email = principal?.payload?.getClaim("email")
                val userType = principal?.payload?.getClaim("userType")
                val userID = principal?.payload?.getClaim("userId")?.asInt()
                val expiresAt = principal?.expiresAt?.time?.minus(System.currentTimeMillis())

                if (principal != null) {
                    call.respondText("Hello, client $email, $userType, $userID, $expiresAt")
                } else {
                    throw BearerStrangeError()
                }
            }
        }
    }
}