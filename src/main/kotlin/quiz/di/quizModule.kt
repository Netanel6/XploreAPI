package org.netanel.quiz.di

import org.koin.dsl.module
import org.netanel.quiz.repository.QuestionsRepository
import org.netanel.quiz.repository.QuestionsRepositoryImpl
import org.netanel.quiz.usecase.GetQuestionsUseCase

val quizModule = module {
    single<QuestionsRepository> { QuestionsRepositoryImpl(get()) }
    factory { GetQuestionsUseCase(get()) }
}