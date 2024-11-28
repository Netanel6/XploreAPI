package org.netanel.data

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document

object MongoDBClient {
    fun createDatabase(): MongoDatabase {
        val connectionString: String = System.getenv("MONGODB_URI")
            ?: "mongodb://localhost:27017"

        val settings: MongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .build()

        val client: MongoClient = MongoClients.create(settings)

        return client.getDatabase("Xplore")
    }
}