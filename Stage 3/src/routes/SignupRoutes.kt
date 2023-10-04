package hotkitchen.routes

import com.typesafe.config.ConfigFactory
import hotkitchen.dto.SignupDTO
import hotkitchen.error.EmailAlreadyRegistered
import hotkitchen.repository.user.UserRepositoryImpl
import hotkitchen.utils.TokenManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Route.signupRoutes() {
    // Instantiate a tokenManager with the config property
    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))
    val userRepository = UserRepositoryImpl()


    route("signup") {
        post {
            val signUpDTO = call.receive<SignupDTO>()

            try {
                val newUser = userRepository.createUser(signUpDTO)
                val token = tokenManager.generateJWTToken(newUser)
                call.respondText(Json.encodeToString(mapOf("token" to token)), status = HttpStatusCode.OK)
            } catch(e: Exception) {
                println(e.message)
            }
        }
    }
}