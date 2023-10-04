package hotkitchen.routes

import hotkitchen.dto.UpdateUserDTO
import hotkitchen.error.WrongIdFormatException
import hotkitchen.repository.user.UserRepositoryImpl
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes() {
    val userRepository = UserRepositoryImpl()

    route("/user") {
        get {
            val users = userRepository.allUsers()
            call.respond(users)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
            val found = userRepository.getUserById(id)
            found.let { call.respond(it) }
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
            val updateUser = call.receive<UpdateUserDTO>()
            call.respond(userRepository.updateUser(id, updateUser))
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
            call.respond(userRepository.deleteUser(id))
        }
    }
}