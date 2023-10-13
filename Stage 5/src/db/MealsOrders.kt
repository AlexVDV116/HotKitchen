package hotkitchen.db

import org.jetbrains.exposed.sql.Table

// intermediate table to store the references of the many-to-many reference
object MealsOrders : Table() {

    val meal = reference("meal", MealTable)
    val order = reference("order", OrderTable)

    override val primaryKey = PrimaryKey(meal, order)
}