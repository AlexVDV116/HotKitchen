package hotkitchen.db.tables

import org.jetbrains.exposed.sql.Table

// intermediate table to store the references of the many-to-many reference

object MealsCategories : Table() {

    val meal = reference("meal", MealTable)

    val category = reference("category", CategoryTable)

    override val primaryKey = PrimaryKey(meal, category)
}