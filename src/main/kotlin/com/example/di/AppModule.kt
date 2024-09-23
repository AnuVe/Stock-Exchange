package com.example.di

import com.example.repository.*
import com.example.services.*
import org.koin.dsl.module

val appModule = module {
    single<UserRepository> { InMemoryUserRepository() }
    single<UserService> { UserServiceImpl(get()) }

    single { OrderBook("AAPL") }
    single<OrderBookRepository> { get<OrderBook>() }
    single<OrderService> { OrderServiceImpl(get()) }
}