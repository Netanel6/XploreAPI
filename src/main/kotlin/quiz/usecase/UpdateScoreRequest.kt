package quiz.usecase

import org.netanel.quiz.repository.QuizRepository
import org.netanel.quiz.repository.model.Quiz
import quiz.repository.model.UpdateScoreRequest


class UpdateScoreUseCase(private val quizRepository: QuizRepository) {
    suspend fun execute(quizId: String, updateScore: UpdateScoreRequest): Quiz? {
        return quizRepository.updateScore(quizId, updateScore)
    }
}
