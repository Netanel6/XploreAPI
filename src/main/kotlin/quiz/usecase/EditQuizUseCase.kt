package org.netanel.quiz.usecase

import org.netanel.quiz.repository.QuizRepository
import org.netanel.quiz.repository.model.Quiz

class EditQuizUseCase(private val quizRepository: QuizRepository) {
    suspend fun execute(quizId: String, quiz: Quiz): Boolean? {
        return quizRepository.editQuiz(quizId, quiz)
    }
}