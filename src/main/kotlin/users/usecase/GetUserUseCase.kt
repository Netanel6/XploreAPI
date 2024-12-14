package org.netanel.users.usecase

import org.netanel.users.repository.model.User
import org.netanel.users.repository.UserRepository

class GetUserUseCase(
    private val userRepository: UserRepository
) {
    suspend fun execute(phoneNumber: String): User? {
        return userRepository.getUserByPhone(phoneNumber)
    }
}