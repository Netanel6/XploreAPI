package org.netanel.users.repository

import org.netanel.users.repository.model.User

interface UserRepository {
    suspend fun getUserByPhone(phoneNumber: String): User?
    suspend fun getAllUsers(): List<User>?
    suspend fun addUser(user: User): Boolean
    suspend fun editUser(userId: String, updatedUser: User): Boolean
    suspend fun deleteUser(userId: String): Boolean
    suspend fun addQuizToUser(userId: String, quiz: User.Quiz): Boolean
    suspend fun deleteQuizForUser(userId: String, quizId: String): Boolean
}