package util

import io.github.cdimascio.dotenv.Dotenv

object EnvironmentConfig {
    private val dotenv = Dotenv.configure()
        .ignoreIfMalformed()
        .ignoreIfMissing()
        .load()

    val enableSsl: String by lazy { dotenv["ENABLE_SSL"] ?: "false" }
    val mongoDbUri: String by lazy { dotenv["MONGODB_URI"] ?: "mongodb+srv://netanelca2:oS29Q3LiSBtazPS4@xplorecluster.jkgx7.mongodb.net" }
    val env: String by lazy { dotenv["ENVIRONMENT"] ?: error("ENVIRONMENT is not defined") }
    val apiUrl: String by lazy { dotenv["API_URL"] ?: error("API_URL is not defined") }
}