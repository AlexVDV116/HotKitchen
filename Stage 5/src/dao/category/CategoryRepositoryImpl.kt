package dao.category

import db.DatabaseFactory.dbQuery
import hotkitchen.dto.category.*
import hotkitchen.error.EntityNotFound
import hotkitchen.models.CategoryEntity
import hotkitchen.models.toCategoryDTO

class CategoryRepositoryImpl : CategoryRepository {
    override suspend fun allCategories(): List<CategoryDTO> = dbQuery {
        CategoryEntity.all().map {
            it.toCategoryDTO()
        }
    }

    override suspend fun getCategoryById(id: CategoryId): CategoryDTO = dbQuery {
        CategoryEntity.findById(id)?.toCategoryDTO() ?: throw EntityNotFound("Category")
    }

    override suspend fun createCategory(category: CreateCategoryDTO): CategoryDTO = dbQuery {
        CategoryEntity.new {
            title = category.title
            description = category.description
        }.toCategoryDTO()
    }

    override suspend fun updateCategory(id: CategoryId, category: CategoryDTO) = dbQuery {
        CategoryEntity.findById(id)?.let {
            it.title = category.title
            it.description = category.description
        } ?: throw EntityNotFound("Category")
    }

    override suspend fun deleteCategory(id: CategoryId) = dbQuery {
        CategoryEntity.findById(id)?.delete() ?: throw EntityNotFound("Category")
    }
}