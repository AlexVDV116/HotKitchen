package hotkitchen.plugins

import hotkitchen.routes.userRoutes
import hotkitchen.routes.signupRoutes
import hotkitchen.routes.signinRoutes
import hotkitchen.routes.validateTokenRoute
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        userRoutes()
        signupRoutes()
        signinRoutes()
        validateTokenRoute()
    }
}

