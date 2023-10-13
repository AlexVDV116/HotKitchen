package hotkitchen.models

import hotkitchen.db.MealTable
import hotkitchen.db.MealsCategories
import hotkitchen.dto.meal.MealDTO
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class MealEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MealEntity>(MealTable)

    var title by MealTable.title
    var price by MealTable.price
    var imageUrl by MealTable.imageUrl

    var categories by CategoryEntity via MealsCategories
}

fun MealEntity.toMealDto() = MealDTO(
    id.value,
    title,
    price,
    imageUrl,
    categories.toList().map { it.id.value }
)