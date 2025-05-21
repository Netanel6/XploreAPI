package org.netanel.di

import org.koin.dsl.module
import org.netanel.mongo.MongoDBClient
import util.JwtConfig

val appModule = module {
    single { MongoDBClient.createDatabase() }
    single {
        JwtConfig(
            secret = "XploreSecretKey",
            issuer = "XploreIssuer",
            audience = "XploreAudience",
            realm = "xplore_realm"
        )
    }
}
