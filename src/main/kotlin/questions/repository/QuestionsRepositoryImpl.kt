package org.netanel.questions.repository

import com.mongodb.client.MongoDatabase
import util.toKotlinObject

class QuestionsRepositoryImpl(val database: MongoDatabase): QuestionsRepository {
    val collection = database.getCollection("test_questions")

    override suspend fun getQuestions(): List<Question> {
        val questions = collection.find().map { it.toKotlinObject<Question>(Question::class.java) }.toList()
        return questions
    }



    override fun addQuestions(questionsList: List<Question>) {
        // Todo: Add functionality via mongoDB
    }

}