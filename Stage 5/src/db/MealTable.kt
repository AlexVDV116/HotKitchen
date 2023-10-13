package hotkitchen.db

import org.jetbrains.exposed.dao.id.IntIdTable

object MealTable : IntIdTable() {

    val title = varchar("title", 100)
    val price = float("price")
    val imageUrl = varchar("imageUrl", 100)
}