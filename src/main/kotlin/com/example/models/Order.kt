package com.example.models

import java.util.Date
import java.util.concurrent.atomic.AtomicInteger

enum class OrderType { BUY, SELL }
enum class OrderStatus { ACCEPTED, EXECUTED, CANCELED }

data class Order(
    var orderId: Int,
    val userId: Int,
    val stockSymbol: String,
    val orderType: OrderType,
    var quantity: Int,
    var price: Double,
    var status: OrderStatus = OrderStatus.ACCEPTED,
    var createdAt: Date
)