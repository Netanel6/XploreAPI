package org.netanel.users.repository

import com.mongodb.client.MongoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.bson.types.ObjectId
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

    override suspend fun deleteUser(userId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val filter = Document("id", userId)
                val result = collection.deleteOne(filter)
                result.deletedCount > 0
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    override suspend fun addQuizToUser(userId: String, quiz: User.Quiz): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val filter = Document("id", userId)
                val userDocument = collection.find(filter).firstOrNull()
                if (userDocument == null) {
                    println("User not found with ID: $userId")
                    return@withContext false
                }
                val updatedQuizList = userDocument["quiz_list"]?.let {
                    (it as List<Document>).toMutableList().apply {
                        add(quiz.toBsonDocument())
                    }
                } ?: listOf(quiz.toBsonDocument())
                val update = Document("\$set", Document("quiz_list", updatedQuizList))
                val result = collection.updateOne(filter, update)
                result.modifiedCount > 0
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}