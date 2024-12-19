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

    override suspend fun addQuizToUser(userId: String, quiz: User.Quiz): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Find the user by id
                val filter = Document("id", userId)
                val userDocument = collection.find(filter).firstOrNull()

                if (userDocument == null) {
                    println("User not found with ID: $userId")
                    return@withContext false
                }

                // Convert the document to User object
                val user = userDocument.toKotlinObject<User>(User::class.java)
                    ?: throw IllegalStateException("Failed to convert user document to User object")

                // Update the quiz list
                val updatedQuizList = user.quiz_list.orEmpty().toMutableList()
                updatedQuizList.add(quiz)

                // Convert the updated quiz list to BSON
                val updatedQuizListBson = updatedQuizList.map { it.toBsonDocument() }

                // Update the user document in the database
                val update = Document("\$set", Document("quiz_list", updatedQuizListBson))
                val result = collection.updateOne(filter, update)

                result.modifiedCount > 0
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

}
