package hotkitchen.routes

import com.typesafe.config.ConfigFactory
import hotkitchen.dao.daoFacadeImpl
import hotkitchen.dto.NewUserDTO
import hotkitchen.dto.UserDTO
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

    route("signup") {
        post {
            val user = call.receive<NewUserDTO>()

            // Check if user email and password match REGEX in NewUserDTO
            // If not return HTTP Status code with status message
            if (user.hasValidCredentials() != "Ok") {
                call.respondText(Json.encodeToString(mapOf("status" to user.hasValidCredentials())), status = HttpStatusCode.Forbidden)
            } else {

                val newUser = daoFacadeImpl.addUser(user.email, user.userType, user.hashedPassword())
                if (newUser != null) {
                    val token = tokenManager.generateJWTToken(newUser)
                    // Return JSON with the token and corresponding HTTP Status Code
                    call.respondText(Json.encodeToString(mapOf("token" to token)), status = HttpStatusCode.OK)
                } else {
                    call.respondText(
                        Json.encodeToString(mapOf("status" to "User already exists")),
                        status = HttpStatusCode.Forbidden
                    )
                }
            }
        }
    }
}