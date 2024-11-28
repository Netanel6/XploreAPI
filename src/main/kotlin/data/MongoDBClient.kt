package org.netanel.data

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import java.util.concurrent.TimeUnit

object MongoDBClient {
    fun createDatabase(): MongoDatabase {
        // Get the MongoDB URI from the environment variable or use a default
        val connectionString: String = System.getenv("MONGODB_URI")
            ?: "mongodb+srv://netanelca2:oS29Q3LiSBtazPS4@xplorecluster.jkgx7.mongodb.net/Xplore?retryWrites=true&w=majority"

        // Configure the MongoClientSettings, including SSL (handled by mongodb+srv:// format)
        val settings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            // Optional: You can configure timeouts if needed
            .applyToClusterSettings { it.serverSelectionTimeout(5000, TimeUnit.MILLISECONDS) }
            .applyToSocketSettings { it.connectTimeout(5000, TimeUnit.MILLISECONDS) }
            .build()

        // Create a MongoClient using the settings
        val client: MongoClient = MongoClients.create(settings)

        // Return the specified database (e.g., "Xplore")
        return client.getDatabase("Xplore")
    }
}
