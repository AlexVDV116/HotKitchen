package hotkitchen.routes.order

import dto.order.OrderDTO
import hotkitchen.error.AuthenticationFailed
import hotkitchen.error.WrongIdFormatException
import repository.order.OrderRepositoryImpl
import repository.user.UserRepositoryImpl
import hotkitchen.error.MissingPermissionError
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.orderRoutes() {
    val orderRepository = OrderRepositoryImpl()
    val userRepository = UserRepositoryImpl()

    authenticate {
        route("/order") {
            post {
                val principal = call.principal<JWTPrincipal>() ?: throw AuthenticationFailed()
                val userId = principal.payload.getClaim("userId")?.asInt() ?: throw AuthenticationFailed()
                val user = userRepository.getUserById(userId)
                val orderDTO = call.receive<OrderDTO>()


                val order = orderRepository.createOrder(orderDTO, user)
                call.respond(order)
            }

            post("/{orderId}/markReady") {
                val principal = call.principal<JWTPrincipal>() ?: throw AuthenticationFailed()
                val userType = principal.payload.getClaim("userType")?.asString() ?: throw AuthenticationFailed()
                val orderId = call.parameters["orderId"]?.toInt() ?: throw WrongIdFormatException()

                if (userType != "STAFF") throw MissingPermissionError("STAFF")
                orderRepository.updateOrder(orderId)
                val updatedOrder = orderRepository.getOrderById(orderId)
                call.respond(updatedOrder)
            }

            get {
                call.principal<JWTPrincipal>() ?: throw AuthenticationFailed()
                val orders = orderRepository.allOrders()
                call.respond(orders)
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
                val found = orderRepository.getOrderById(id)
                found.let { call.respond(it) }
            }

            get("/orderHistory") {
                call.principal<JWTPrincipal>() ?: throw AuthenticationFailed()
                val orders = orderRepository.allOrders()
                call.respond(orders)
            }

            get("/orderIncomplete") {
                call.principal<JWTPrincipal>() ?: throw AuthenticationFailed()
                val incompleteOrders = orderRepository.allIncompleteOrders()
                call.respond(incompleteOrders)
            }
        }
    }
}