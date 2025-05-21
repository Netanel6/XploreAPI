package org.netanel.users.usecase

import org.netanel.users.repository.UserRepository
import org.netanel.users.repository.model.User

class EditUserUseCase(private val userRepository: UserRepository) {
    suspend fun execute(userId:String, updatedUser: User): Boolean {
        return userRepository.editUser(userId, updatedUser)
    }
}