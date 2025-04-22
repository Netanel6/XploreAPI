package org.netanel.mongo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import util.EnvironmentConfig
import javax.net.ssl.SSLContext

object MongoDBClient {
    fun createDatabase(): MongoDatabase {
        val connectionString: String = EnvironmentConfig.get("MONGODB_URI", "mongodb://localhost")
        val enableSsl: Boolean = EnvironmentConfig.getBoolean("ENABLE_SSL", false       )

        val builder = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .applyToSslSettings { ssl ->
                ssl.enabled(enableSsl)
                if (enableSsl) {
                    val sslContext = SSLContext.getInstance("TLS").apply {
                        init(null, arrayOf(TrustAllCertificates()), java.security.SecureRandom())
                    }
                    ssl.context(sslContext)
                }
            }

        val settings = builder.build()
        val client: MongoClient = MongoClients.create(settings)
        return client.getDatabase("Xplore")
    }

    class TrustAllCertificates : javax.net.ssl.X509TrustManager {
        override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>?, authType: String?) {}
        override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
    }
}

