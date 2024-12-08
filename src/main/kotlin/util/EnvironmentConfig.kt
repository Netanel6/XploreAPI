package util

import io.github.cdimascio.dotenv.Dotenv

object EnvironmentConfig {
    private val dotenv = Dotenv.configure()
        .ignoreIfMalformed()
        .ignoreIfMissing()
        .load()

    val enableSsl: String by lazy { dotenv["ENABLE_SSL"] ?: "false" }
    val mongoDbUri: String by lazy { dotenv["MONGODB_URI"] ?: error("MONGODB_URI is not defined") }
    val env: String by lazy { dotenv["ENVIRONMENT"] ?: error("ENVIRONMENT is not defined") }
    val apiUrl: String by lazy { dotenv["API_URL"] ?: error("API_URL is not defined") }
}