package org.netanel.users.di

import org.koin.dsl.module
import org.netanel.users.repository.UserRepository
import org.netanel.users.repository.UserRepositoryImpl
import org.netanel.users.usecase.GetUserUseCase
import users.usecase.AddUserUseCase
import users.usecase.GetAllUsersUseCase
import users.usecase.UpdateUserUseCase


val usersModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
    factory { GetUserUseCase(get()) }
    factory { GetAllUsersUseCase(get()) }
    factory { AddUserUseCase(get()) }
    factory { UpdateUserUseCase(get()) }
}