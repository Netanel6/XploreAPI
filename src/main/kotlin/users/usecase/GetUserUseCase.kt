package org.netanel.users.usecase

import org.netanel.users.repository.model.User
import org.netanel.users.repository.AuthRepository

class GetUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun execute(phoneNumber: String): User? {
        return authRepository.getUser(phoneNumber)
    }
}