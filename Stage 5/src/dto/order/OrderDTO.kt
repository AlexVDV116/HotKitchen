package dto.order

import kotlinx.serialization.Serializable

typealias OrderId = Int

@Serializable
data class OrderDTO(
    val orderId: Int = 0,
    val userEmail: String = "",
    val mealIds: List<Int>?,
    val price: Float = -1f,
    val address: String = "",
    val status: String = ""
)