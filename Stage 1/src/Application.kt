package hotkitchen


import hotkitchen.plugins.configureRouting
import io.ktor.server.application.*
import hotkitchen.dao.*
import hotkitchen.plugins.configureSerialization

// CMD + Shift + N or Tools -> HTTP Client to create a new scratch file and test HTTP requests
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    configureRouting()
    configureSerialization()
    DatabaseFactory.init()
}