package hotkitchen.routes

import hotkitchen.dao.daoFacadeImpl
import hotkitchen.dto.CreateUserDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

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