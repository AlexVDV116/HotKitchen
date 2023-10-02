package hotkitchen.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Route.validateTokenRoute() {
    authenticate() {
        route("validate") {
            get {
                val principal = call.principal<JWTPrincipal>()
                val email = principal!!.payload.getClaim("email").asString()
                val userID = principal!!.payload.getClaim("id").asInt()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())

                if (principal != null) {
                    call.respondText("Hello, client $email")
                } else {
                    call.respondText(Json.encodeToString(mapOf("Authorization" to "Bearer something.very.strange")), status = HttpStatusCode.Forbidden)
                }
            }
        }
    }
}