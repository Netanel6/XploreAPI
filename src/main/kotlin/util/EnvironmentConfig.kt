package util

import io.github.cdimascio.dotenv.Dotenv
import org.koin.core.logger.Logger

object EnvironmentConfig {
    private val dotenv: Dotenv = Dotenv.configure()
        .ignoreIfMalformed() // Ignore malformed .env files
        .ignoreIfMissing()   // Ignore missing .env files in production
        .load()

    fun get(key: String, defaultValue: String? = null): String {
        return dotenv[key]
            ?: System.getenv(key)
            ?: defaultValue
            ?: throw IllegalArgumentException("Environment variable '$key' is not set.")
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return dotenv[key]?.equals("true", ignoreCase = true)
            ?: System.getenv(key)?.equals("true", ignoreCase = true)
            ?: defaultValue
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return dotenv[key]?.toIntOrNull()
            ?: System.getenv(key)?.toIntOrNull()
            ?: defaultValue
    }
}
