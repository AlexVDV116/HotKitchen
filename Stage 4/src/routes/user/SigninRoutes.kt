package hotkitchen.routes.user

import com.typesafe.config.ConfigFactory
import hotkitchen.dto.user.SigninDTO
import hotkitchen.error.AuthenticationFailed
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

fun Route.signinRoutes() {
    // Instantiate a tokenManager with the config property
    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))

    route("signin") {
        post("signin") {
            val userRepository = UserRepositoryImpl()
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
    }
}