package com.example.services

import com.example.models.User
import com.example.repository.UserRepository

interface UserService {
    fun registerUser(user: User)
    fun getUserById(userId: Int): User?
}

class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    override fun registerUser(user: User) {
        userRepository.addUser(user)
    }

    override fun getUserById(userId: Int): User? {
        return userRepository.findUserById(userId)
    }
}