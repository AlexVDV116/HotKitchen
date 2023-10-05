package hotkitchen.routes.user

import hotkitchen.dto.user.SignupDTO
import hotkitchen.dto.user.UpdateUserDTO
import hotkitchen.error.AuthenticationFailed
import hotkitchen.error.EmailUpdateAttemptError
import hotkitchen.error.UserNotFound
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
        route("/me") {
            get {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asInt() ?: throw AuthenticationFailed()
                val user = userRepository.getUserById(userId)
                call.respond(user)
            }

            put {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asInt() ?: throw AuthenticationFailed()
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

            delete {
                val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
                call.respond(userRepository.deleteUser(id))
            }
        }
    }
}
