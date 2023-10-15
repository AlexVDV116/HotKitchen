package hotkitchen.db.dao

import dto.order.OrderDTO
import hotkitchen.db.tables.MealsOrders
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import hotkitchen.db.tables.OrderTable

enum class OrderStatus(val status: String) {
    IN_PROGRESS("IN PROGRESS"),
    COMPLETE("COMPLETE")
}

class OrderEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderEntity>(OrderTable)

    var userEmail by OrderTable.userEmail
    var price by OrderTable.price
    var address by OrderTable.address
    var status by OrderTable.status

    // many-to-many reference trough intermediate table MealsOrders
    var meals by MealEntity via MealsOrders
}


fun OrderEntity.toOrderDTO() = OrderDTO(
    id.value,
    userEmail,
    meals.toList().map { it.id.value },
    price,
    address,
    status.status
)