package hotkitchen.routes

import hotkitchen.dao.daoFacadeImpl
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class Status (val status: String)

fun Route.userRoutes() {
    route("users") {
        get {
            val users = daoFacadeImpl.allUsers()
            call.respond(users)
        }
    }
}