package com.example

import com.example.controllers.orderRoutes
import com.example.controllers.userRoutes
import com.example.di.appModule
import com.example.services.OrderService
import com.example.services.UserService
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain
        .main(args)
}

@Suppress("unused")
fun Application.module() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    install(Koin) {
        modules(appModule)
    }

    val userService: UserService by inject()
    val orderService: OrderService by inject()

    routing {
        userRoutes(userService)
        orderRoutes(orderService)
    }
}
