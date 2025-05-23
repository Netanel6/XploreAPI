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
import quiz.repository.model.UpdateScoreRequest
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

    override suspend fun editQuiz(quizId: String, updatedQuiz: Quiz): Boolean? {
        val quizCollection = database.getCollection("quiz")
        return withContext(Dispatchers.IO) {
            try {
                val filter = quizCollection.find(eq("_id", ObjectId(quizId))).firstOrNull() ?: return@withContext null
                val update = Document("\$set", updatedQuiz.toBsonDocument())
                quizCollection.updateOne(filter, update).wasAcknowledged()
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    override suspend fun getQuizFoListForUser(userId: String): List<User.Quiz> {
        val userCollection = database.getCollection("users")

        return withContext(Dispatchers.IO) {

            val userDocument = userCollection.find(Document("id", userId)).firstOrNull()
            if (userDocument == null) {
                println("User not found with ID: $userId")
                return@withContext emptyList()
            }

            val quizList = userDocument.getList("quiz_list", Document::class.java)
                .mapNotNull { it.toKotlinObject<User.Quiz>(User.Quiz::class.java) }

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

    override suspend fun updateScore(quizId: String, updateScore: UpdateScoreRequest): Quiz? {
        val collection = database.getCollection("quiz")

        val filter = Document("_id", ObjectId(quizId))
        val quizDocument = collection.find(filter).firstOrNull() ?: return null

        val scoreBoard = quizDocument.get("scoreBoard", Document::class.java)
        val scoresList = scoreBoard?.getList("scores", Document::class.java) ?: mutableListOf()

        val updated = scoresList.map {
            if (it.getString("id") == updateScore.userId) {
                it["score"] = updateScore.score
            }
            it
        }

        scoreBoard["scores"] = updated

        val update = Document("\$set", Document("scoreBoard", scoreBoard))
        collection.updateOne(filter, update)

        return collection.find(filter).firstOrNull()?.toKotlinObject(Quiz::class.java)
    }
}