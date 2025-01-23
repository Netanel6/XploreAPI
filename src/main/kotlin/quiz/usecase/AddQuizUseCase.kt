package quiz.usecase

import org.netanel.quiz.repository.QuizRepository
import org.netanel.quiz.repository.model.Quiz

class AddQuizUseCase(private val quizRepository: QuizRepository) {
    suspend fun execute(quiz: Quiz): Boolean {
        return quizRepository.addQuiz(quiz)
    }
}
