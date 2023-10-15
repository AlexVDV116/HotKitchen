package hotkitchen.plugins

import hotkitchen.routes.*
import hotkitchen.routes.category.categoryRoutes
import hotkitchen.routes.meal.mealRoutes
import hotkitchen.routes.order.orderRoutes
import hotkitchen.routes.user.userRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.configureRouting() {

    routing {
        userRoutes()
        validateTokenRoute()
        mealRoutes()
        categoryRoutes()
        orderRoutes()
    }
}

