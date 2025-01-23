package quiz.usecase

import org.netanel.quiz.repository.QuizRepository
import org.netanel.quiz.repository.model.Quiz

class GetQuizListUseCase(private val quizRepository: QuizRepository) {
    suspend fun execute(): List<Quiz> {
        return quizRepository.getQuizList()
    }
}