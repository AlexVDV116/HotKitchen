package hotkitchen

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.ConfigFactory
import hotkitchen.plugins.configureRouting
import io.ktor.server.application.*
import hotkitchen.dao.*
import hotkitchen.plugins.configureSecurity
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
    embeddedServer(Netty, port = 28852, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module(testing: Boolean = false) {
    DatabaseFactory.init()

    // Order should be security - serialization - routing
    configureSecurity()
    configureSerialization()
    configureRouting()
}