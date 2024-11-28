package org.netanel.questions.usecase

import org.netanel.questions.repository.Question
import org.netanel.questions.repository.QuestionsRepository

class GetQuestionsUseCase(
    private val questionRepository: QuestionsRepository
) {
    suspend fun execute(): List<Question> {
        return questionRepository.getQuestions()
    }
}