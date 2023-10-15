package repository.meal

import hotkitchen.db.DatabaseFactory.dbQuery
import hotkitchen.db.tables.CategoryTable
import hotkitchen.db.dao.CategoryEntity
import hotkitchen.db.dao.MealEntity
import hotkitchen.db.dao.toMealDto
import hotkitchen.dto.meal.MealDTO
import hotkitchen.dto.meal.MealId
import hotkitchen.error.EntityNotFound
import org.jetbrains.exposed.sql.SizedCollection

class MealRepositoryImpl : MealRepository {
    override suspend fun allMeals(): List<MealDTO> = dbQuery {
        MealEntity.all().map {
            it.toMealDto()
        }
    }

    override suspend fun getMealById(id: MealId): MealDTO = dbQuery {
        MealEntity.findById(id)?.toMealDto() ?: throw EntityNotFound("Meal")
    }

    override suspend fun createMeal(meal: MealDTO): MealDTO = dbQuery {
        val categoryIds = meal.categoryIds?.toList() ?: throw EntityNotFound("Category")
        val categoryList = CategoryEntity.find { CategoryTable.id inList categoryIds }.toList()
        if(categoryList.isEmpty() || categoryList.size != categoryIds.size) throw EntityNotFound("Category")

        MealEntity.new {
            title = meal.title
            price = meal.price
            imageUrl = meal.imageUrl
            categories = SizedCollection(categoryList)
        }.toMealDto()
    }

    override suspend fun updateMeal(id: MealId, meal: MealDTO) = dbQuery {
        val categoryIds = meal.categoryIds?.toList() ?: throw EntityNotFound("Category")
        val categoryList = CategoryEntity.find { CategoryTable.id inList categoryIds }.toList()
        MealEntity.findById(id)?.let {
            it.title = meal.title
            it.price = meal.price
            it.imageUrl = meal.imageUrl
            it.categories = SizedCollection(categoryList)
        } ?: throw EntityNotFound("Meal")
    }

    override suspend fun deleteMeal(id: MealId) = dbQuery {
        MealEntity.findById(id)?.delete() ?: throw EntityNotFound("Meal")
    }
}