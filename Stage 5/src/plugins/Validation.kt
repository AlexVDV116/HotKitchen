package hotkitchen.plugins

import hotkitchen.dto.category.CreateCategoryDTO
import hotkitchen.dto.meal.CreateMealDTO
import hotkitchen.dto.user.SignupDTO
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureValidation() {
    install(RequestValidation) {

        validate<SignupDTO> { user ->
            try {
                user.validate()
                ValidationResult.Valid
            } catch (e: Exception) {
                ValidationResult.Invalid(e.message!!)
            }
        }

        validate<CreateMealDTO> {
            try {
                it.validate()
                ValidationResult.Valid
            } catch (e: Exception) {
                ValidationResult.Invalid(e.message!!)
            }
        }

        validate<CreateCategoryDTO> {
            try {
                it.validate()
                ValidationResult.Valid
            } catch (e: Exception) {
                ValidationResult.Invalid(e.message!!)
            }
        }
    }
}