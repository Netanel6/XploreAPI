package users.usecase

import org.netanel.users.repository.AuthRepository
import org.netanel.users.repository.model.User

class GetAllUsersUseCase(private val authRepository: AuthRepository) {
    suspend fun execute(): List<User>? {
        return authRepository.getAllUsers()
    }
}