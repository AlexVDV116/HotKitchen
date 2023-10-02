package hotkitchen.routes

import com.typesafe.config.ConfigFactory
import hotkitchen.dao.daoFacadeImpl
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

fun Route.signinRoutes() {
    // Instantiate a tokenManager with the config property
    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))

    route("signin") {
        post {
            // Receives user credentials sent as a JSON object and converts it to a
            // SigninUserDto data class object
            val user = call.receive<UserDTO>()

            // Check if user email and password match REGEX in UserDTO
            // If not return HTTP Status code with status message
            if (user.hasValidCredentials() != "Ok") {
                call.respondText(
                    Json.encodeToString(mapOf("status" to user.hasValidCredentials())),
                    status = HttpStatusCode.Forbidden
                )
            } else {

                // Authenticate the user, if authenticated returns a user object
                val authenticatedUser = daoFacadeImpl.authenticateUser(user.email, user.password)
                // If the user is authenticated generate a JWT token
                if (authenticatedUser != null) {
                    val token = tokenManager.generateJWTToken(authenticatedUser)
                    // Return JSON with the token and corresponding HTTP Status Code
                    call.respondText(Json.encodeToString(mapOf("token" to token)), status = HttpStatusCode.OK)
                } else {
                    call.respondText(
                        Json.encodeToString(mapOf("status" to "Invalid email or password")),
                        status = HttpStatusCode.Forbidden
                    )
                }
            }
        }
    }
}