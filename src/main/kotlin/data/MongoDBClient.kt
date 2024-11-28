package data

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import com.mongodb.connection.SslSettings
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager

object MongoDBClient {
    fun createDatabase(): MongoDatabase {
        // Use the MongoDB URI from the environment variable, or fall back to localhost
        val connectionString: String = System.getenv("MONGODB_URI")
            ?: "mongodb+srv://netanelca2:oS29Q3LiSBtazPS4@xplorecluster.jkgx7.mongodb.net"

        // Initialize SSL context with a custom TrustManager to trust all certificates (useful for development)
        val sslContext: SSLContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf<TrustManager>(TrustAllCertificates()), java.security.SecureRandom())

        // Create MongoClientSettings with custom SSL settings
        val settings: MongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .applyToSslSettings {
                it.enabled(true)  // Ensure SSL is enabled
                it.context(sslContext)  // Apply custom SSLContext
            }
            .build()

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
