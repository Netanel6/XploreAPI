package org.netanel.quiz.usecase

import org.netanel.quiz.repository.QuizRepository
import org.netanel.quiz.repository.model.Quiz

class GetQuizUseCase(
    private val quizRepository: QuizRepository
) {
    suspend fun execute(quizId: String): Quiz? {
        return quizRepository.getQuiz(quizId)
    }
}