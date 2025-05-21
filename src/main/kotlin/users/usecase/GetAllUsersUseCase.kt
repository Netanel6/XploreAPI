package users.usecase

import org.netanel.users.repository.UserRepository
import org.netanel.users.repository.model.User

class GetAllUsersUseCase(private val userRepository: UserRepository) {
    suspend fun execute(): List<User>? {
        return userRepository.getAllUsers()
    }
}