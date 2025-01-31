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
                // Create filter to find the user by id
                val filter = Document("id", userId)

                // Find the specific user in the users collection
                val userDocument = collection.find(filter).firstOrNull()

                if (userDocument == null) {
                    println("User not found with ID: $userId")
                    return@withContext false
                }

                // Add the quiz to the existing quiz list or initialize the list if it doesn't exist
                val updatedQuizList = userDocument["quiz_list"]?.let {
                    // If quiz_list exists, cast it to a mutable list and add the new quiz
                    (it as List<Document>).toMutableList().apply {
                        add(quiz.toBsonDocument())
                    }
                } ?: listOf(quiz.toBsonDocument()) // If quiz_list doesn't exist, initialize it with the new quiz

                // Update the user document in the database with the new quiz list
                val update = Document("\$set", Document("quiz_list", updatedQuizList))
                val result = collection.updateOne(filter, update)

                // Return true if the user document was successfully updated
                result.modifiedCount > 0
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

}
