package users.usecase

import org.netanel.users.repository.UserRepository
import org.netanel.users.repository.model.User

class AssignQuizForUserUseCase(private val userRepository: UserRepository) {
    suspend fun execute(userId: String, quizzes: List<User.Quiz>): Boolean {
        return userRepository.addQuizToUser(userId, quizzes)
    }
}

