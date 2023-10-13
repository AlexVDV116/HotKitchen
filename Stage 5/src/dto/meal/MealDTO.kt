package hotkitchen.dto.meal

import hotkitchen.error.InvalidFieldError
import kotlinx.serialization.Serializable

typealias MealId = Int

@Serializable
data class MealDTO(
    val mealId: Int = 0,
    val title: String = "",
    val price: Float = -1f,
    val imageUrl: String = "",
    val categoryIds: List<Int>?
) {
    fun validate() {
        val missing = mapOf("title" to title, "imageUrl" to imageUrl).filter { it.value.isEmpty() }.keys.firstOrNull()
        if (missing != null) {
            throw InvalidFieldError(missing)
        }
    }
}