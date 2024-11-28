package data

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object MongoDBClient {
    fun createDatabase(): MongoDatabase {
        val connectionString: String = System.getenv("MONGODB_URI")
            ?: "mongodb+srv://localhost:27017"  // Example for MongoDB Atlas

        // Initialize the SSL context with a custom TrustManager to trust all certificates
        val sslContext: SSLContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf<TrustManager>(TrustAllCertificates()), java.security.SecureRandom())

        // Create MongoClientSettings with custom SSL settings
        val settings: MongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .applyToSslSettings { sslBuilder ->
                sslBuilder.enabled(true)  // Keep SSL enabled
                sslBuilder.context(sslContext)  // Apply the custom SSLContext
                sslBuilder.invalidHostNameAllowed(true)  // Allow invalid hostnames (for dev purposes)
            }
            .build()

        // Create MongoClient with the above settings
        val client: MongoClient = MongoClients.create(settings)

        // Return the database connection
        return client.getDatabase("Xplore")
    }

    // Custom TrustManager that accepts all certificates (for dev purposes)
    class TrustAllCertificates : X509TrustManager {
        override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>?, authType: String?) {}
        override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate>? = null
    }
}
