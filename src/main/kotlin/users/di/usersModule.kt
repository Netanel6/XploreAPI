package org.netanel.users.di

import org.koin.dsl.module
import org.netanel.users.repository.UserRepository
import org.netanel.users.repository.UserRepositoryImpl
import org.netanel.users.usecase.DeleteUserUseCase
import org.netanel.users.usecase.EditUserUseCase
import org.netanel.users.usecase.GetUserUseCase
import users.usecase.AddUserUseCase
import users.usecase.GetAllUsersUseCase
import users.usecase.AssignQuizForUserUseCase
import users.usecase.DeleteQuizForUserUseCase


val usersModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
    factory { GetUserUseCase(get()) }
    factory { GetAllUsersUseCase(get()) }
    factory { AddUserUseCase(get()) }
    factory { EditUserUseCase(get()) }
    factory { DeleteUserUseCase(get()) }
    factory { AssignQuizForUserUseCase(get()) }
    factory { DeleteQuizForUserUseCase(get()) }
}