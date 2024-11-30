package org.netanel.users.repository

import org.netanel.users.repository.model.User

interface AuthRepository {
    suspend fun getUser(phoneNumber: String): User?
}