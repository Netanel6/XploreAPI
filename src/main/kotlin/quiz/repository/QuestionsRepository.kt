package org.netanel.quiz.repository

import org.netanel.quiz.repository.model.Question

interface QuestionsRepository {
    suspend fun getQuestions(): List<Question>
     fun addQuestions(questionsList: List<Question>)
}

