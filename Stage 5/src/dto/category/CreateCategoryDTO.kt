package hotkitchen.dto.category

import hotkitchen.error.InvalidFieldError
import kotlinx.serialization.Serializable

typealias CategoryId = Int

@Serializable
data class CreateCategoryDTO(
    val title: String,
    val description: String
) {
    fun validate() {
        val missing = mapOf("title" to title, "description" to description).filter { it.value.isEmpty() }.keys.firstOrNull()
        if (missing != null) {
            throw InvalidFieldError(missing)
        }
    }
}