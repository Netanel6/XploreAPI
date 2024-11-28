package org.netanel.questions.repository

interface QuestionsRepository {
    suspend fun getQuestions(): List<Question>
     fun addQuestions(questionsList: List<Question>)
}

