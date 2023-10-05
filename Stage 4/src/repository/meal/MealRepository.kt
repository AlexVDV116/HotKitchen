package hotkitchen.repository.meal

import hotkitchen.dto.Meal.MealDTO
import hotkitchen.dto.Meal.MealId

interface MealRepository {
    suspend fun allMeals(): List<MealDTO>
    suspend fun getMealById(id: MealId): MealDTO
    suspend fun createMeal(meal: MealDTO): MealDTO
    suspend fun updateMeal(id: MealId, meal: MealDTO)
    suspend fun deleteMeal(id: MealId)
}