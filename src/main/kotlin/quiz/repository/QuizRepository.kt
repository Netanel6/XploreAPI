package org.netanel.quiz.repository

import org.netanel.quiz.repository.model.Question
import org.netanel.quiz.repository.model.Quiz
import org.netanel.users.repository.model.User
import quiz.repository.model.UpdateScoreRequest


interface QuizRepository {
    suspend fun getQuestions(): List<Question>
    suspend fun getQuiz(quizId: String): Quiz?
    suspend fun getQuizList(): List<Quiz>
    suspend fun editQuiz(quizId: String, updatedQuiz: Quiz): Boolean?
    suspend fun getQuizFoListForUser(userId: String): List<User.Quiz>
    suspend fun addQuiz(quiz: Quiz): Boolean
    suspend fun updateScore(quizId: String, updateScore: UpdateScoreRequest): Quiz?
}

