package com.example.services

import com.example.models.Order
import com.example.models.OrderStatus
import com.example.models.Trade
import com.example.repository.OrderBookRepository

interface OrderService {
    fun placeOrder(order: Order): List<Trade>
    fun cancelOrder(order: Order)
    fun getOrderStatus(orderId: Int): Order?
    fun updateOrder(order: Order): Order?
}

class OrderServiceImpl(private val orderBook: OrderBookRepository) : OrderService {
    override fun placeOrder(order: Order): List<Trade> {
        println(order.orderId)
        return orderBook.addOrder(order)
    }

    override fun cancelOrder(order: Order) {
        orderBook.cancelOrder(order)
    }

    override fun getOrderStatus(orderId: Int): Order? {
        return orderBook.getOrderStatus(orderId)
    }

    override fun updateOrder(order: Order): Order? {
        val existingOrder = getOrderStatus(order.orderId) ?: return null
        if (existingOrder.status != OrderStatus.ACCEPTED) return null
        existingOrder.price = order.price
        existingOrder.quantity = order.quantity
        return existingOrder
    }
}