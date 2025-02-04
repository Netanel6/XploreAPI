package users.usecase

import org.netanel.users.repository.UserRepository

class DeleteQuizForUserUseCase(private val userRepository: UserRepository) {
    suspend fun execute(userId: String, quizId: String): Boolean {
        return userRepository.deleteQuizForUser(userId, quizId)
    }
}