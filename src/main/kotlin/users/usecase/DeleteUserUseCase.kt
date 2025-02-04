package org.netanel.users.usecase

import org.netanel.users.repository.UserRepository

class DeleteUserUseCase(private val userRepository: UserRepository) {
    suspend fun execute(userId: String): Boolean {
        return userRepository.deleteUser(userId)
    }
}