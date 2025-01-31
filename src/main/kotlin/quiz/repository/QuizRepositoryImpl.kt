package org.netanel.quiz.repository

import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.bson.types.ObjectId
import org.netanel.quiz.repository.model.Question
import org.netanel.quiz.repository.model.Quiz
import org.netanel.users.repository.model.User
import util.toBsonDocument
import util.toKotlinObject

class QuizRepositoryImpl(private val database: MongoDatabase) : QuizRepository {


    override suspend fun getQuestions(): List<Question> {
        val collection = database.getCollection("test_questions")
        return withContext(Dispatchers.IO) {
            val questions = collection.find().map { it.toKotlinObject<Question>(Question::class.java) }.toList()
            questions
        }
    }

    override suspend fun getQuiz(quizId: String): Quiz? {
        val quizCollection = database.getCollection("quiz")
        return withContext(Dispatchers.IO) {
            val quizDocument = quizCollection.find(eq("_id", ObjectId(quizId))).firstOrNull() ?: return@withContext null

            val quiz = quizDocument.toKotlinObject<Quiz>(Quiz::class.java)
            quiz
        }
    }

    override suspend fun getQuizList(): List<Quiz> {
        val quizCollection = database.getCollection("quiz")
        return withContext(Dispatchers.IO) {
            quizCollection.find()
                .map { it.toKotlinObject<Quiz>(Quiz::class.java) }
                .toList()
        }
    }

    override suspend fun getQuizFoListForUser(userId: String): List<User.Quiz> {
        val userCollection = database.getCollection("users") // Fetch from "users" collection

        return withContext(Dispatchers.IO) {

            val userDocument = userCollection.find(Document("token", userId)).firstOrNull()
            if (userDocument == null) {
                println("User not found with ID: $userId")
                return@withContext emptyList()
            }

            val quizList = userDocument.getList("quiz_list", Document::class.java)
                .mapNotNull { it.toKotlinObject<User.Quiz>(User.Quiz::class.java) } // Convert BSON to Kotlin objects

            return@withContext quizList
        }
    }


    override suspend fun addQuiz(quiz: Quiz): Boolean {
        val quizCollection = database.getCollection("quiz")
        return withContext(Dispatchers.IO) {
            val document = quiz.toBsonDocument()
            quizCollection.insertOne(document).wasAcknowledged()
        }
    }
}