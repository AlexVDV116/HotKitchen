package hotkitchen.models

import hotkitchen.dto.Category.CategoryDTO
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class CategoryEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CategoryEntity>(CategoryTable)

    var title by CategoryTable.title
    var description by CategoryTable.description
}

object CategoryTable : IntIdTable() {

    val title = varchar("title", 100)
    val description = varchar("title", 100)
}

fun CategoryEntity.toCategoryDTO() = CategoryDTO(
    this.id.value,
    this.title,
    this.description
)