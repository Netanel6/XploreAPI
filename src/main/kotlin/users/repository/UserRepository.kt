package org.netanel.users.repository

import org.netanel.users.repository.model.User

interface UserRepository {
    suspend fun getUserByPhone(phoneNumber: String): User?
    suspend fun getAllUsers(): List<User>?
    suspend fun addUser(user: User): Boolean
}