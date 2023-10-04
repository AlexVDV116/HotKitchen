package hotkitchen.routes

import hotkitchen.dto.UpdateUserDTO
import hotkitchen.error.AuthenticationFailed
import hotkitchen.error.WrongIdFormatException
import hotkitchen.repository.user.UserRepositoryImpl
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.meRoutes() {
    val userRepository = UserRepositoryImpl()

    authenticate {
        get("me") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("userId")?.asInt() ?: throw AuthenticationFailed()
            val user = userRepository.getUserById(userId)
            call.respond(user)
        }

        put("me") {
            val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
            val updateUser = call.receive<UpdateUserDTO>()
            call.respond(userRepository.updateUser(id, updateUser))
        }

        delete("me") {
            val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
            call.respond(userRepository.deleteUser(id))
        }
    }
}