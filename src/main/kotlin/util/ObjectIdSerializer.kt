package util

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import org.bson.types.ObjectId

@Serializer(forClass = ObjectId::class)
object ObjectIdSerializer : KSerializer<ObjectId> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ObjectId", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ObjectId {
        val json = decoder as? JsonDecoder
            ?: throw SerializationException("This class can be deserialized only by Json")
        val jsonObject = json.decodeJsonElement().jsonObject
        val oid = jsonObject["\$oid"]?.jsonPrimitive?.content
            ?: throw SerializationException("Expected \$oid field")
        return ObjectId(oid)
    }

    override fun serialize(encoder: Encoder, value: ObjectId) {
        val json = encoder as? JsonEncoder
            ?: throw SerializationException("This class can be serialized only by Json")
        json.encodeString(value.toString())
    }
}
