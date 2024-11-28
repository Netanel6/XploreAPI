package org.netanel.questions.di

import org.koin.dsl.module
import org.netanel.questions.repository.QuestionsRepository
import org.netanel.questions.repository.QuestionsRepositoryImpl
import org.netanel.questions.usecase.GetQuestionsUseCase


val questionsModule = module {
    single<QuestionsRepository> { QuestionsRepositoryImpl(get()) }
    factory { GetQuestionsUseCase(get()) }
}