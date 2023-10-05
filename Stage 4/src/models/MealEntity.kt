package hotkitchen.models

import hotkitchen.dto.Meal.MealDTO
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class MealEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MealEntity>(MealTable)

    var title by MealTable.title
    var price by MealTable.price
    var imageUrl by MealTable.imageUrl
    var categoryIds by MealTable.categoryIds
}

object MealTable : IntIdTable() {

    val title = varchar("title", 100)
    val price = float("price")
    val imageUrl = varchar("imageUrl", 100)
    val categoryIds = integer("categoryIds")
}

fun MealEntity.toMealDto() = MealDTO(
    this.id.value,
    this.title,
    this.price,
    this.imageUrl,
    this.categoryIds
)