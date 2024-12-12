package org.netanel.users.di

import org.koin.dsl.module
import org.netanel.users.repository.AuthRepository
import org.netanel.users.repository.AuthRepositoryImpl
import org.netanel.users.usecase.GetUserUseCase
import users.usecase.GetAllUsersUseCase


val usersModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    factory { GetUserUseCase(get()) }
    factory { GetAllUsersUseCase(get()) }
}