package hotkitchen.routes

import hotkitchen.dao.daoFacadeImpl
import hotkitchen.dto.SigninUserDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Route.signinRoutes() {
    route("signin") {
        post {
            val user = call.receive<SigninUserDto>()
            val checkUser = daoFacadeImpl.signinUser(user.email, user.password)
            // If the signinUser method returns true
            if (checkUser) {
                // Return the status as a serialized data class with a response code
                val status = Status("Signed In")
                call.respondText(Json.encodeToString(status), status = HttpStatusCode.OK)
            } else {
                val status = Status("Authorization failed")
                call.respondText(Json.encodeToString(status), status = HttpStatusCode.Forbidden)
            }
        }
    }
}