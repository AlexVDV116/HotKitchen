package hotkitchen.routes.user

import com.typesafe.config.ConfigFactory
import hotkitchen.dto.user.SigninDTO
import hotkitchen.dto.user.SignupDTO
import hotkitchen.dto.user.UpdateUserDTO
import hotkitchen.error.*
import dao.user.UserRepositoryImpl
import hotkitchen.utils.TokenManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Route.userRoutes() {
    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))
    val userRepository = UserRepositoryImpl()

    post("signup") {
        val signUpDTO = call.receive<SignupDTO>()

        val newUser = userRepository.createUser(signUpDTO)
        val token = tokenManager.generateJWTToken(newUser)

        call.response.header("Authorization", token)
        call.respondText(Json.encodeToString(mapOf("token" to token)), status = HttpStatusCode.OK)

    }

    post("signin") {
        val signInDTO = call.receive<SigninDTO>()

        try {
            val authenticatedUser = userRepository.authenticateUser(signInDTO)
            val token = tokenManager.generateJWTToken(authenticatedUser)

            call.response.header("Authorization", token)
            call.respondText(Json.encodeToString(mapOf("token" to token)), status = HttpStatusCode.OK)
        } catch (e: Exception) {
            throw AuthenticationFailed()
        }
    }

    authenticate {
        get("user") {
            val principal = call.principal<JWTPrincipal>() ?: throw AuthenticationFailed()
            val userType = principal.payload.getClaim("userType")?.asString() ?: throw AuthenticationFailed()

            if (userType != "STAFF") throw MissingPermissionError("STAFF")
            val users = userRepository.allUsers()
            call.respond(users)
        }

        get("user/{id}") {
            val principal = call.principal<JWTPrincipal>() ?: throw AuthenticationFailed()
            val userType = principal.payload.getClaim("userType")?.asString() ?: throw AuthenticationFailed()

            if (userType != "STAFF") throw MissingPermissionError("STAFF")
            val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
            val found = userRepository.getUserById(id)
            found.let { call.respond(it) }
        }

        put("user/{id}") {
            val principal = call.principal<JWTPrincipal>() ?: throw AuthenticationFailed()
            val userType = principal.payload.getClaim("userType")?.asString() ?: throw AuthenticationFailed()

            if (userType != "STAFF") throw MissingPermissionError("STAFF")
            val userId = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
            val updateUserDTO = call.receive<UpdateUserDTO>()

            try {
                val existingUser = userRepository.getUserById(userId)
                if (existingUser.email != updateUserDTO.email) throw EmailUpdateAttemptError()
                userRepository.updateUser(userId, updateUserDTO)
                val updatedUser = userRepository.getUserById(userId)
                call.respond(updatedUser)
            } catch (e: EntityNotFound) {
                val signUpDto = SignupDTO(updateUserDTO.email, updateUserDTO.userType, updateUserDTO.password)
                val newUser = userRepository.createUser(signUpDto)
                call.respond(newUser)
            }
        }

        delete("user/{id}") {
            val principal = call.principal<JWTPrincipal>() ?: throw AuthenticationFailed()
            val userType = principal.payload.getClaim("userType")?.asString() ?: throw AuthenticationFailed()

            if (userType != "STAFF") throw MissingPermissionError("STAFF")
            val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
            call.respond(userRepository.deleteUser(id))
        }

        get("/me") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("userId")?.asInt() ?: throw AuthenticationFailed()
            val user = userRepository.getUserById(userId)
            call.respond(user)
        }

        put("/me") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("userId")?.asInt() ?: throw AuthenticationFailed()
            val updateUserDTO = call.receive<UpdateUserDTO>()

            try {
                val existingUser = userRepository.getUserById(userId)
                if (existingUser.email != updateUserDTO.email) throw EmailUpdateAttemptError()
                userRepository.updateUser(userId, updateUserDTO)
                val updatedUser = userRepository.getUserById(userId)
                call.respond(updatedUser)
            } catch (e: EntityNotFound) {
                val signUpDto = SignupDTO(updateUserDTO.email, updateUserDTO.userType, updateUserDTO.password)
                val newUser = userRepository.createUser(signUpDto)
                call.respond(newUser)
            }
        }

        delete("/me") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("userId")?.asInt() ?: throw AuthenticationFailed()
            call.respond(userRepository.deleteUser(userId))
        }
    }
}