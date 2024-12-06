package org.netanel.quiz.repository

import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.types.ObjectId
import org.netanel.quiz.repository.model.Question
import org.netanel.quiz.repository.model.Quiz
import util.toKotlinObject

class QuizRepositoryImpl(val database: MongoDatabase): QuizRepository {


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

            /* // Fetch associated questions from the database
            val questionCollection = database.getCollection("test_questions")
            val questions = questionCollection.find(eq("quizId", quizId))
                .map { it.toKotlinObject<Question>(Question::class.java) }
                .toList()*/
            quiz
        }
    }
}