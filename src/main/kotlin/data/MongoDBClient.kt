package data

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import com.mongodb.connection.SslSettings
import javax.net.ssl.SSLContext

object MongoDBClient {
    fun createDatabase(): MongoDatabase {
        val connectionString: String = System.getenv("MONGODB_URI")
            ?: "mongodb://localhost:27017"

        val sslContext: SSLContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf<javax.net.ssl.TrustManager>(TrustAllCertificates()), java.security.SecureRandom())

        val settings: MongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .applyToSslSettings {
                SslSettings.builder().enabled(true).context(sslContext).build()
            }
            .build()

        val client: MongoClient = MongoClients.create(settings)

        return client.getDatabase("Xplore")
    }

    class TrustAllCertificates : javax.net.ssl.X509TrustManager {
        override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>?, authType: String?) {}
        override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate>? = null
    }
}
