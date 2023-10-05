package hotkitchen.routes.category

import hotkitchen.dto.Category.CategoryDTO
import hotkitchen.error.AuthenticationFailed
import hotkitchen.error.WrongIdFormatException
import hotkitchen.error.WrongUserType
import hotkitchen.repository.category.CategoryRepositoryImpl
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.categoryRoutes() {
    val categoryRepository = CategoryRepositoryImpl()

    authenticate {
        route("/categories") {
            post {
                val principal = call.principal<JWTPrincipal>() ?: throw AuthenticationFailed()
                val userType = principal.payload.getClaim("userType")?.asString() ?: throw AuthenticationFailed()
                val categoryDTO = call.receive<CategoryDTO>()

                if (userType != "staff") throw WrongUserType()
                categoryRepository.createCategory(categoryDTO)
            }

            get {
                call.principal<JWTPrincipal>() ?: throw AuthenticationFailed()
                val categories = categoryRepository.allCategories()
                call.respond(categories)
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
                val found = categoryRepository.getCategoryById(id)
                found.let { call.respond(it) }
            }
        }
    }
}