package com.example.repository
import com.example.models.User

interface UserRepository {
    fun addUser(user: User)

    fun findUserById(userId: Int): User?
}

class InMemoryUserRepository : UserRepository {
    private val users = mutableMapOf<Int, User>()

    override fun addUser(user: User) {
        users[user.userId] = user
    }

    override fun findUserById(userId: Int): User? = users[userId]
}
