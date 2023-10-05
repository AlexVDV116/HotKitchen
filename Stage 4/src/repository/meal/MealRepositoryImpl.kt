package hotkitchen.repository.meal

import hotkitchen.dto.Meal.MealDTO
import hotkitchen.dto.Meal.MealId
import hotkitchen.error.MealNotFound
import hotkitchen.models.MealEntity
import hotkitchen.models.toMealDto
import org.jetbrains.exposed.sql.transactions.transaction

class MealRepositoryImpl : MealRepository {
    override suspend fun allMeals(): List<MealDTO> = transaction {
        MealEntity.all().map {
            it.toMealDto()
        }
    }

    override suspend fun getMealById(id: MealId): MealDTO = transaction {
        MealEntity.findById(id)?.toMealDto() ?: throw MealNotFound()
    }

    override suspend fun createMeal(meal: MealDTO): MealDTO = transaction {
        MealEntity.new {
            title = meal.title
            price = meal.price
            imageUrl = meal.imageUrl
            categoryIds = meal.categoryIds
        }.toMealDto()
    }

    override suspend fun updateMeal(id: MealId, meal: MealDTO) = transaction {
        MealEntity.findById(id)?.let {
            it.title = meal.title
            it.price = meal.price
            it.imageUrl = meal.imageUrl
            it.categoryIds = meal.categoryIds
        } ?: throw MealNotFound()
    }

    override suspend fun deleteMeal(id: MealId) = transaction {
        MealEntity.findById(id)?.delete() ?: throw MealNotFound()
    }
}