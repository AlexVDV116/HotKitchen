package dao.order

import dto.order.OrderDTO
import dto.order.OrderId
import hotkitchen.dto.user.UserDTO

interface OrderRepository {
    suspend fun allOrders(): List<OrderDTO>
    suspend fun allIncompleteOrders(): List<OrderDTO>
    suspend fun getOrderById(id: OrderId): OrderDTO
    suspend fun createOrder(order: OrderDTO, user: UserDTO): OrderDTO
    suspend fun updateOrder(id: OrderId)
    suspend fun deleteOrder(id: OrderId)
}