package org.netanel.di

import org.koin.dsl.module
import org.netanel.data.MongoDBClient

val appModule = module {
    single { MongoDBClient.createDatabase() }
}