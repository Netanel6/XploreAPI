package users.usecase

import org.netanel.users.repository.UserRepository
import org.netanel.users.repository.model.User

class AddUserUseCase(private val userRepository: UserRepository) {
    suspend fun execute(user: User): Boolean {
        return userRepository.addUser(user)
    }
}