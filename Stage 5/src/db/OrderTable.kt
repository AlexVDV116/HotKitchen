package hotkitchen.db

import hotkitchen.models.OrderStatus
import org.jetbrains.exposed.dao.id.IntIdTable

object OrderTable : IntIdTable() {
    val userEmail = varchar("userEmail", 100)
    val price = float("price")
    val address = varchar("address", 100)
    val status = enumerationByName<OrderStatus>("status", 100)
}