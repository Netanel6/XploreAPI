package org.netanel.users.repository.model

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import util.ObjectIdSerializer

@Serializable
data class User(
    @Serializable(with = ObjectIdSerializer::class)
    val _id: ObjectId? = null,
    val name: String,
    val phone_number: String,
    val quiz_list: List<Quiz>? = null,
    val token: String? = null
) {
    @Serializable
    data class Quiz(val id: String, val title: String)
}
