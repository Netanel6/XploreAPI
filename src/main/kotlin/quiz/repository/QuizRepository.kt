package org.netanel.quiz.repository

import org.netanel.quiz.repository.model.Question
import org.netanel.quiz.repository.model.Quiz

interface QuizRepository {
    suspend fun getQuestions(): List<Question>
    suspend fun getQuiz(quizId: String): Quiz?
}

