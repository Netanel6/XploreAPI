package users.usecase

import org.netanel.users.repository.AuthRepository
import org.netanel.users.repository.model.User

class AddUserUseCase(private val authRepository: AuthRepository) {
    suspend fun execute(user: User): Boolean {
        return authRepository.addUser(user)
    }
}