package hotkitchen.repository.category

import hotkitchen.dto.Category.CategoryDTO
import hotkitchen.dto.Category.CategoryId
import hotkitchen.error.CategoryNotFound
import hotkitchen.models.CategoryEntity
import hotkitchen.models.toCategoryDTO
import org.jetbrains.exposed.sql.transactions.transaction

class CategoryRepositoryImpl : CategoryRepository {
    override suspend fun allCategories(): List<CategoryDTO> = transaction {
        CategoryEntity.all().map {
            it.toCategoryDTO()
        }
    }

    override suspend fun getCategoryById(id: CategoryId): CategoryDTO = transaction {
        CategoryEntity.findById(id)?.toCategoryDTO() ?: throw CategoryNotFound()
    }

    override suspend fun createCategory(category: CategoryDTO): CategoryDTO = transaction {
        CategoryEntity.new {
            title = category.title
            description = category.description
        }.toCategoryDTO()
    }

    override suspend fun updateCategory(id: CategoryId, category: CategoryDTO) = transaction {
        CategoryEntity.findById(id)?.let {
            it.title = category.title
            it.description = category.description
        } ?: throw CategoryNotFound()
    }

    override suspend fun deleteCategory(id: CategoryId) = transaction {
        CategoryEntity.findById(id)?.delete() ?: throw CategoryNotFound()
    }
}