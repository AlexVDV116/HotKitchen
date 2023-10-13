package dao.category

import hotkitchen.dto.category.CategoryDTO
import hotkitchen.dto.category.CategoryId
import hotkitchen.dto.category.CreateCategoryDTO

interface CategoryRepository {
    suspend fun allCategories(): List<CategoryDTO>
    suspend fun getCategoryById(id: CategoryId): CategoryDTO
    suspend fun createCategory(category: CreateCategoryDTO): CategoryDTO
    suspend fun updateCategory(id: CategoryId, category: CategoryDTO)
    suspend fun deleteCategory(id: CategoryId)
}