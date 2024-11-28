package util

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.bson.Document

inline fun <reified T> T.toBsonDocument(): Document {
    val json = Json { ignoreUnknownKeys = true }.encodeToString(serializer(), this)
    return Document.parse(json)
}

inline fun <reified T> Document.toKotlinObject(clazz: Class<T>): T {
    val jsonString = this.toJson() // Converts the Document to a JSON string
    return Json.decodeFromString(jsonString) // Deserialize it into the Kotlin object
}
