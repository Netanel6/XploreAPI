package org.netanel.quiz.repository.model

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import util.ObjectIdSerializer


@Serializable
data class Question(
    @Serializable(with = ObjectIdSerializer::class)
    val _id: ObjectId? = null,
    var id: Int? = null,
    val media: String? = null,
    val text: String? = null,
    val answers: List<String>? = null,
    val correctAnswerIndex: Int? = null,
    val type: QuestionType? = null
) {
    @Serializable
    enum class QuestionType {
        American, TrueOrFalse
    }
}

/*  "id": 11,
    "text": "של איזו חברה היה הרכב הראשון של אור?",
    "answers": ["כתום מזעזע", "כחול מטאלי", "כחול נייבי", "אוף-וויט"],
    "correctAnswerIndex": 1,
    "type": "American"
*/