package org.netanel.quiz.usecase

import org.netanel.quiz.repository.model.Question
import org.netanel.quiz.repository.QuizRepository

class GetQuestionsUseCase(
    private val questionRepository: QuizRepository
) {
    suspend fun execute(): List<Question> {
        return questionRepository.getQuestions()
    }
}