package util

import io.github.cdimascio.dotenv.Dotenv

object EnvironmentConfig {
    private val dotenv: Dotenv = Dotenv.configure()
        .ignoreIfMalformed() // Ignore malformed .env files
        .ignoreIfMissing()   // Ignore missing .env files in production
        .load()

    fun get(key: String, defaultValue: String? = null): String {
        return dotenv[key] ?: System.getenv(key) ?: defaultValue
            ?: throw IllegalArgumentException("Environment variable '$key' is not set.")
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return dotenv[key]?.toBooleanStrictOrNull()
            ?: System.getenv(key)?.toBooleanStrictOrNull()
            ?: defaultValue
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return dotenv[key]?.toIntOrNull()
            ?: System.getenv(key)?.toIntOrNull()
            ?: defaultValue
    }
}