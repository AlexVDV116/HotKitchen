import hotkitchen.dao.daoFacadeImpl
import hotkitchen.dto.CreateUserDto
import hotkitchen.routes.Status
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Status (val status: String)


fun Route.signupRoutes() {
    route("signup") {
        post {
            val user = call.receive<CreateUserDto>()
            val newUser = daoFacadeImpl.addUser(user.email, user.userType, user.password)
            if (newUser != null) {
                // Return the status as a serialized data class with a response code
                val status = Status("Signed Up")
                call.respondText(Json.encodeToString(status), status = HttpStatusCode.OK)
            } else {
                val status = Status("Registration failed")
                call.respondText(Json.encodeToString(status), status = HttpStatusCode.Forbidden)
            }
        }
    }
}