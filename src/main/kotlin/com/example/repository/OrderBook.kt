package com.example.repository

import com.example.models.Order
import com.example.models.OrderStatus
import com.example.models.OrderType
import com.example.models.Trade
import java.util.Date
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock

private val orderIdGenerator = AtomicInteger(0)

fun generateOrderId(): Int = orderIdGenerator.incrementAndGet()

interface OrderBookRepository {
    fun addOrder(order: Order): List<Trade>

    fun cancelOrder(order: Order)

    fun matchOrders(): List<Trade>

    fun getOrderStatus(orderId: Int): Order?
}

class OrderBook(
    private val stockSymbol: String,
) : OrderBookRepository {
    private val buyOrders = mutableListOf<Order>()
    private val sellOrders = mutableListOf<Order>()
    private val lock = ReentrantLock()
    private val tradeIdGenerator = AtomicInteger(0)

    override fun addOrder(order: Order): List<Trade> {
        lock.lock()
        order.status = OrderStatus.ACCEPTED
        try {
            order.orderId = generateOrderId()
            order.createdAt = Date()
            when (order.orderType) {
                OrderType.BUY -> buyOrders.add(order)
                OrderType.SELL -> sellOrders.add(order)
            }
            println(order)
            return matchOrders()
        } finally {
            lock.unlock()
        }
    }

    override fun cancelOrder(order: Order) {
        lock.lock()
        try {
            when (order.orderType) {
                OrderType.BUY -> buyOrders.remove(order)
                OrderType.SELL -> sellOrders.remove(order)
            }
            order.status = OrderStatus.CANCELED
        } finally {
            lock.unlock()
        }
    }

    override fun matchOrders(): List<Trade> {
        val trades = mutableListOf<Trade>()
        println(buyOrders)
        println(sellOrders)
        while (buyOrders.isNotEmpty() || sellOrders.isNotEmpty()) {
            val buyOrder = buyOrders.maxByOrNull { it.price }
            val sellOrder = sellOrders.minByOrNull { it.price }

            if (buyOrder != null && sellOrder != null && buyOrder.price == sellOrder.price) {
                val tradeQuantity = minOf(buyOrder.quantity, sellOrder.quantity)
                val tradePrice = sellOrder.price
                trades.add(
                    Trade(
                        buyerOrderId = buyOrder.orderId,
                        sellerOrderId = sellOrder.orderId,
                        stockSymbol = buyOrder.stockSymbol,
                        quantity = tradeQuantity,
                        price = tradePrice,
                        tradeId = tradeIdGenerator.incrementAndGet(),
                        createdAt = Date(),
                    ),
                )

                buyOrder.quantity -= tradeQuantity
                buyOrder.status = OrderStatus.EXECUTED
                sellOrder.quantity -= tradeQuantity
                sellOrder.status = OrderStatus.EXECUTED

                if (buyOrder.quantity == 0) buyOrders.remove(buyOrder)

                if (sellOrder.quantity == 0) sellOrders.remove(sellOrder)
            } else {
                break
            }
        }
        return trades
    }

    override fun getOrderStatus(orderId: Int): Order? {
        lock.lock()
        try {
            return (buyOrders + sellOrders).find { it.orderId == orderId }
        } finally {
            lock.unlock()
        }
    }
}
