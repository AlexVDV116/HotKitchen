package hotkitchen.routes.user

import hotkitchen.dto.user.SignupDTO
import hotkitchen.dto.user.UpdateUserDTO
import hotkitchen.error.EmailUpdateAttemptError
import hotkitchen.error.UserNotFound
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
            val userId = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
            val updateUserDTO = call.receive<UpdateUserDTO>()

            try {
                val existingUser = userRepository.getUserById(userId)
                if (existingUser.email != updateUserDTO.email) throw EmailUpdateAttemptError()
                userRepository.updateUser(userId, updateUserDTO)
                val updatedUser = userRepository.getUserById(userId)
                call.respond(updatedUser)
            } catch (e: UserNotFound) {
                val signUpDto = SignupDTO(updateUserDTO.email, updateUserDTO.userType, updateUserDTO.password)
                val newUser = userRepository.createUser(signUpDto)
                call.respond(newUser)
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
            call.respond(userRepository.deleteUser(id))
        }
    }
}