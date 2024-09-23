package com.example.controllers

import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import com.example.models.Order
import com.example.services.OrderService
import io.ktor.http.*

fun Route.orderRoutes(orderService: OrderService) {
    route("/orders") {
        post("/place") {
            val order = call.receive<Order>()
            val trades = orderService.placeOrder(order)
            call.respondText("Order placed successfully, trades: $trades", status = HttpStatusCode.Created)
        }

        get("/{id}") {
            val orderId = call.parameters["id"]?.toIntOrNull()
            if (orderId != null) {
                val order = orderService.getOrderStatus(orderId)
                if (order != null) {
                    call.respond(order)
                } else{
                    call.respondText("Order not found", status = HttpStatusCode.NotFound)
                }
            } else{
                call.respondText("Invalid order ID", status = HttpStatusCode.BadRequest)
            }
        }

        post("/cancel") {
            val order = call.receive<Order>()
            orderService.cancelOrder(order)
            call.respondText("Order canceled successfully", status = HttpStatusCode.OK)
        }

        put("/modify") {
            val order = call.receive<Order>()
            val modifiedOrder = orderService.updateOrder(order)
            if (modifiedOrder != null) {
                call.respond(modifiedOrder)
            } else {
                call.respondText("Order not found or cannot be modified", status = HttpStatusCode.BadRequest)
            }
        }
    }
}