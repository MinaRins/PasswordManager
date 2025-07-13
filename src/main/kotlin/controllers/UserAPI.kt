package controllers

import models.User

class UserAPI {

    private val users = ArrayList<User>()

    fun add(user: User): Boolean = users.add(user)

    fun listAll(): List<User> = users

    fun findById(id: Int): User? = users.find { it.userId == id }

    fun delete(id: Int): Boolean {
        val user = findById(id)
        return if (user != null) users.remove(user) else false
    }

    fun isValidUserId(id: Int): Boolean = users.any { it.userId == id }

    fun login(email: String, password: String): User? {
        return users.find { it.email.equals(email, ignoreCase = true) && it.masterPassword == password }
    }

    fun numberOfUsers(): Int = users.size
}
