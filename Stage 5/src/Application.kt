package hotkitchen

import db.DatabaseFactory
import hotkitchen.plugins.configureRouting
import io.ktor.server.application.*
import hotkitchen.error.configureStatusPages
import hotkitchen.plugins.configureSecurity
import hotkitchen.plugins.configureSerialization
import hotkitchen.plugins.configureValidation
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(
        Netty,
        port = 28852,
        host = "0.0.0.0",
        module = Application::module)
        .start(wait = true)
}

fun Application.module(testing: Boolean = false) {
    configureSecurity()
    configureSerialization()
    configureRouting()
    configureStatusPages()
    configureValidation()
    DatabaseFactory.init()


//    TODO("SLIDES LES 7, USERPROFILE JSON ATTRIBUTE FOR LOCATION (LAT/LONG)")
//    TODO("SEPARATE USER AND USERPROFILE ROUTES")
//    TODO("CLEAN UP ERRORS / STATUS PAGES")
//    TODO("SEPARATE DB TABLES FROM ENTITIES / MAKE DB PACKAGE WITH DAO AND DTO AND TABLES")
//    TODO("CLEAN EACH FILE")
//    TODO("DOCUMENT APP LOGIC + WORKFLOW")
}