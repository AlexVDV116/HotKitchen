package hotkitchen.models

import hotkitchen.db.CategoryTable
import hotkitchen.dto.category.CategoryDTO
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CategoryEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CategoryEntity>(CategoryTable)

    var title by CategoryTable.title
    var description by CategoryTable.description
}

fun CategoryEntity.toCategoryDTO() = CategoryDTO(
    this.id.value,
    this.title,
    this.description
)