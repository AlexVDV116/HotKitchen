package repository.meal

import hotkitchen.dto.meal.MealDTO
import hotkitchen.dto.meal.MealId

interface MealRepository {
    suspend fun allMeals(): List<MealDTO>
    suspend fun getMealById(id: MealId): MealDTO
    suspend fun createMeal(meal: MealDTO): MealDTO
    suspend fun updateMeal(id: MealId, meal: MealDTO)
    suspend fun deleteMeal(id: MealId)
}