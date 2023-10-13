package dao.order

import dto.order.OrderDTO
import dto.order.OrderId
import db.DatabaseFactory.dbQuery
import hotkitchen.db.MealTable
import hotkitchen.dto.user.UserDTO
import hotkitchen.error.EntityNotFound
import hotkitchen.models.*
import org.jetbrains.exposed.sql.SizedCollection
import hotkitchen.db.OrderTable

class OrderRepositoryImpl : OrderRepository {

    override suspend fun allOrders(): List<OrderDTO> = dbQuery {
        OrderEntity.all().map {
            it.toOrderDTO()
        }
    }

    override suspend fun allIncompleteOrders(): List<OrderDTO> = dbQuery {
        OrderEntity.find { OrderTable.status eq OrderStatus.IN_PROGRESS }.map {
            it.toOrderDTO()
        }
    }

    override suspend fun getOrderById(id: OrderId): OrderDTO = dbQuery {
        OrderEntity.findById(id)?.toOrderDTO() ?: throw EntityNotFound("Order")
    }

    override suspend fun createOrder(order: OrderDTO, user: UserDTO): OrderDTO = dbQuery {
        val mealIds = order.mealIds?.toList() ?: throw EntityNotFound("Meal")
        val mealList = MealEntity.find { MealTable.id inList mealIds }.toList()
        if(mealList.isEmpty() || mealList.size != mealIds.size) throw EntityNotFound("Meal")

        OrderEntity.new {
            userEmail = user.email
            meals = SizedCollection(mealList)
            price = order.price
            address = user.address
            status = OrderStatus.IN_PROGRESS
        }.toOrderDTO()
    }

    override suspend fun updateOrder(id: OrderId) = dbQuery {
        OrderEntity.findById(id)?.let {
            it.status = OrderStatus.COMPLETE
        } ?: throw EntityNotFound("Order")
    }

    override suspend fun deleteOrder(id: OrderId) = dbQuery {
        OrderEntity.findById(id)?.delete() ?: throw EntityNotFound("Order")
    }
}