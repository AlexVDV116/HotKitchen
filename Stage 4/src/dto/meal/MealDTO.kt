package hotkitchen.dto.Meal

import kotlinx.serialization.Serializable

@Serializable
data class MealDTO(
    val mealId: Int,
    val title: String,
    val price: Float,
    val imageUrl: String,
    val categoryIds: Int,
)