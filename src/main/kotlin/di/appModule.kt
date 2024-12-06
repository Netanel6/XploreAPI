package org.netanel.di

import org.koin.dsl.module
import org.netanel.mongo.MongoDBClient

val appModule = module {
    single { MongoDBClient.createDatabase() }
}