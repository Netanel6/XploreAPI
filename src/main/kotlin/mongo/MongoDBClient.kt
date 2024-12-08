package org.netanel.mongo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import util.EnvironmentConfig
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager

object MongoDBClient {

    fun createDatabase(): MongoDatabase {
        val connectionString: String = System.getenv("MONGODB_URI")?: EnvironmentConfig.mongoDbUri
        val enableSSL: Boolean = System.getenv("ENABLE_SSL").toBoolean() ?: EnvironmentConfig.enableSsl.toBoolean()

        // Initialize SSL context only if SSL is enabled
        val sslContext: SSLContext? = if (enableSSL) {
            SSLContext.getInstance("TLS").apply {
                init(null, arrayOf<TrustManager>(TrustAllCertificates()), java.security.SecureRandom())
            }
        } else {
            null
        }

        // Create MongoClientSettings with or without SSL
        val settingsBuilder = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))

        if (enableSSL && sslContext != null) {
            settingsBuilder.applyToSslSettings {
                it.enabled(true)
                it.context(sslContext)  // Apply custom SSLContext
            }
        } else {
            settingsBuilder.applyToSslSettings {
                it.enabled(false)  // Disable SSL
            }
        }

        val settings: MongoClientSettings = settingsBuilder.build()

        // Create MongoClient with the settings
        val client: MongoClient = MongoClients.create(settings)

        // Return the "Xplore" database from MongoDB
        return client.getDatabase("Xplore")
    }

    // TrustManager that accepts all certificates (for dev purposes)
    class TrustAllCertificates : javax.net.ssl.X509TrustManager {
        override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>?, authType: String?) {}
        override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate>? = null
    }
}