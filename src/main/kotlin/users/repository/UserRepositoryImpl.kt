package org.netanel.users.repository

import com.mongodb.client.MongoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.netanel.users.repository.model.User
import util.toBsonDocument
import util.toKotlinObject

class UserRepositoryImpl(val database: MongoDatabase) : UserRepository {
    private val collection = database.getCollection("users")

    override suspend fun getUserByPhone(phoneNumber: String): User? {
        return withContext(Dispatchers.IO) {
            val document = collection.find(Document("phone_number", phoneNumber)).firstOrNull()
            document?.toKotlinObject<User>(User::class.java)
        }
    }

    override suspend fun getAllUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            val documents = collection.find().toList()
            documents.mapNotNull { document ->
                document.toKotlinObject<User>(User::class.java)
            }
        }
    }


    override suspend fun addUser(user: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val document = user.toBsonDocument()
                collection.insertOne(document)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

}
