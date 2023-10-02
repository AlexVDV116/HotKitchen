package hotkitchen

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.ConfigFactory
import hotkitchen.plugins.configureRouting
import io.ktor.server.application.*
import hotkitchen.dao.*
import hotkitchen.plugins.configureSerialization
import hotkitchen.utils.TokenManager
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// CMD + Shift + N or Tools -> HTTP Client to create a new scratch file and test HTTP requests
fun main() {
    embeddedServer(Netty, port = 28852, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module(testing: Boolean = false) {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val tokenManager = TokenManager(config)

    install(Authentication) {
        jwt {
            verifier(tokenManager.verifyJWTToken())
            val realm = config.property("jwt.realm").getString()

            validate { jwtCredential ->
                if (jwtCredential.payload.getClaim("email").asString().isNotEmpty()) {
                    JWTPrincipal(jwtCredential.payload)
                } else null
            }

            challenge { _, _ ->
                call.respondText(Json.encodeToString(mapOf("Authorization" to "Bearer something.very.strange")), status = HttpStatusCode.Unauthorized)
            }

        }
    }
    configureSerialization()
    configureRouting()
    DatabaseFactory.init()

}