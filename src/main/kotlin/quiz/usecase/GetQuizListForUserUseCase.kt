package org.netanel.quiz.usecase

import org.netanel.quiz.repository.QuizRepository
import org.netanel.quiz.repository.model.Quiz
import org.netanel.users.repository.model.User

class GetQuizListForUserUseCase(private val quizRepository: QuizRepository) {
    suspend fun execute(userId: String): List<User.Quiz> {
        return quizRepository.getQuizFoListForUser(userId)
    }
}