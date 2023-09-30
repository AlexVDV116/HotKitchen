package hotkitchen.plugins

import hotkitchen.routes.signinRoutes
import hotkitchen.routes.userRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import signupRoutes


fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        userRoutes()
        signupRoutes()
        signinRoutes()
    }
}

