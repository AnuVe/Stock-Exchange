package com.example.services

import com.example.models.Trade

interface TradeService {
    fun getTradeHistory(stockSymbol: String): List<Trade>
}

class TradeServiceImpl(private val trades: MutableList<Trade>) : TradeService {
    override fun getTradeHistory(stockSymbol: String): List<Trade> {
        return trades.filter { it.stockSymbol == stockSymbol }
    }
}