package hotkitchen.routes.meal

import hotkitchen.dto.meal.MealDTO
import hotkitchen.error.AuthenticationFailed
import hotkitchen.error.WrongIdFormatException
import repository.meal.MealRepositoryImpl
import hotkitchen.error.MissingPermissionError
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.mealRoutes() {
    val mealRepository = MealRepositoryImpl()

    authenticate {
        route("/meals") {
            post {
                val principal = call.principal<JWTPrincipal>() ?: throw AuthenticationFailed()
                val userType = principal.payload.getClaim("userType")?.asString() ?: throw AuthenticationFailed()
                val mealDTO = call.receive<MealDTO>()

               if (userType != "STAFF") throw MissingPermissionError("STAFF")
                val meal = mealRepository.createMeal(mealDTO)
                call.respond(meal)
            }

            get {
                call.principal<JWTPrincipal>() ?: throw AuthenticationFailed()
                val meals = mealRepository.allMeals()
                call.respond(meals)
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
                val found = mealRepository.getMealById(id)
                found.let { call.respond(it) }
            }
        }
    }
}