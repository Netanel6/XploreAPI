package model

import kotlinx.serialization.Serializable

@Serializable
data class ServerResponse<T>(
    val status: String,
    val code: Int,
    val message: String? = null,
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T, code: Int): ServerResponse<T> {
            return ServerResponse(
                status = "success",
                code = code,
                data = data
            )
        }

        fun error(message: String, code: Int): ServerResponse<Nothing> {
            return ServerResponse(
                status = "error",
                code = code,
                message = message
            )
        }
    }
}