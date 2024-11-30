package org.netanel.quiz.repository

import com.mongodb.client.MongoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.netanel.quiz.repository.model.Question
import util.toKotlinObject

class QuestionsRepositoryImpl(val database: MongoDatabase): QuestionsRepository {
    val collection = database.getCollection("test_questions")

    override suspend fun getQuestions(): List<Question> {
        return withContext(Dispatchers.IO) {
            val questions = collection.find().map { it.toKotlinObject<Question>(Question::class.java) }.toList()
            questions
        }
    }



    override fun addQuestions(questionsList: List<Question>) {
        // Todo: Add functionality via mongoDB
    }

}