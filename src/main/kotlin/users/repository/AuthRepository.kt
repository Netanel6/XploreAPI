package org.netanel.users.repository

import org.netanel.users.model.User

interface AuthRepository {
    suspend fun getUser(phoneNumber: String): User?
}