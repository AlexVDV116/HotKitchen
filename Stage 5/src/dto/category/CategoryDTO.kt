package hotkitchen.dto.category

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDTO(
    val categoryId: Int,
    val title: String,
    val description: String
)