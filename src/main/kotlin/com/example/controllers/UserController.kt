package com.example.controllers

import com.example.models.User
import com.example.services.UserService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes(userService: UserService) {
    route("/users") {
        post("/register") {
            val user = call.receive<User>()
            userService.registerUser(user)
            call.respondText("User registered successfully", status = HttpStatusCode.Created)
        }

        get("/{id}") {
            val userId = call.parameters["id"]?.toIntOrNull()
            val user = userId?.let { userService.getUserById(it) }
            if (user != null) {
                call.respond(user)
            } else {
                call.respondText("User not found", status = HttpStatusCode.NotFound)
            }
        }
    }
}
