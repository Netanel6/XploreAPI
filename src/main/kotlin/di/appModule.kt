package org.netanel.di

import data.MongoDBClient
import org.koin.dsl.module

val appModule = module {
    single { MongoDBClient.createDatabase() }
}