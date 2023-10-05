package hotkitchen.repository.category

import hotkitchen.dto.Category.CategoryDTO
import hotkitchen.dto.Category.CategoryId

interface CategoryRepository {
    suspend fun allCategories(): List<CategoryDTO>
    suspend fun getCategoryById(id: CategoryId): CategoryDTO
    suspend fun createCategory(category: CategoryDTO): CategoryDTO
    suspend fun updateCategory(id: CategoryId, category: CategoryDTO)
    suspend fun deleteCategory(id: CategoryId)
}