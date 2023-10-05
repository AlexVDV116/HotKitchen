package hotkitchen.routes.meal

import hotkitchen.dto.Meal.MealDTO
import hotkitchen.error.AuthenticationFailed
import hotkitchen.error.WrongIdFormatException
import hotkitchen.error.WrongUserType
import hotkitchen.repository.meal.MealRepositoryImpl
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
            post("meals") {
                val principal = call.principal<JWTPrincipal>() ?: throw AuthenticationFailed()
                val userType = principal.payload.getClaim("userType")?.asString() ?: throw AuthenticationFailed()
                val mealDTO = call.receive<MealDTO>()

                if (userType != "staff") throw WrongUserType()
                mealRepository.createMeal(mealDTO)
            }

            get("meals") {
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