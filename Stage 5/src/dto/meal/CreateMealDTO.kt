package hotkitchen.dto.meal

import hotkitchen.error.InvalidFieldError
import kotlinx.serialization.Serializable

@Serializable
data class CreateMealDTO(
    val title: String,
    val price: Float,
    val imageUrl: String,
    val categoryIds: Int,
) {
    fun validate() {
        val missing = mapOf("title" to title, "imageUrl" to imageUrl).filter { it.value.isEmpty() }.keys.firstOrNull()
        if (missing != null) {
            throw InvalidFieldError(missing)
        }
    }
}