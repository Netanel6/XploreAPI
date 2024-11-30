package org.netanel.users.model

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import util.ObjectIdSerializer

@Serializable
data class User(
    @Serializable(with = ObjectIdSerializer::class)
    val _id: ObjectId? = null,
    val name: String,
    val phone_number: String
)