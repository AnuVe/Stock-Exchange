package com.example.models

import java.util.Date

data class Trade(
    val tradeId: Int,
    val buyerOrderId: Int,
    val sellerOrderId: Int,
    val stockSymbol: String,
    val quantity: Int,
    val price: Double,
    val createdAt: Date
)