package org.netanel.users.repository

import com.mongodb.client.MongoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.netanel.users.model.User
import util.toKotlinObject

class AuthRepositoryImpl(val database: MongoDatabase) : AuthRepository {
    private val collection = database.getCollection("users")

    override suspend fun getUser(phoneNumber: String): User? {
        return withContext(Dispatchers.IO) {
            val document = collection.find(Document("phone_number", phoneNumber)).firstOrNull()
            document?.toKotlinObject<User>(User::class.java)
        }
    }
}