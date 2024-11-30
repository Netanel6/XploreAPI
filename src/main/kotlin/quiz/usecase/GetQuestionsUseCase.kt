package org.netanel.quiz.usecase

import org.netanel.quiz.repository.model.Question
import org.netanel.quiz.repository.QuestionsRepository

class GetQuestionsUseCase(
    private val questionRepository: QuestionsRepository
) {
    suspend fun execute(): List<Question> {
        return questionRepository.getQuestions()
    }
}