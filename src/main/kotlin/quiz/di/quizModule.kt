package org.netanel.quiz.di

import org.koin.dsl.module
import org.netanel.quiz.repository.QuizRepository
import org.netanel.quiz.repository.QuizRepositoryImpl
import org.netanel.quiz.usecase.GetQuestionsUseCase
import org.netanel.quiz.usecase.GetQuizUseCase
import quiz.usecase.GetQuizListUseCase

val quizModule = module {
    single<QuizRepository> { QuizRepositoryImpl(get()) }
    factory { GetQuestionsUseCase(get()) }
    factory { GetQuizUseCase(get()) }
    factory { GetQuizListUseCase(get()) }
}