package org.netanel.quiz.repository.model

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import quiz.repository.model.DifficultyLevel
import util.ObjectIdSerializer

@Serializable
data class Quiz(
    @Serializable(with = ObjectIdSerializer::class)
    val _id: ObjectId? = null,
    val questions: List<QuestionV2>,
    val timer: Int,
    val title: String? = null,
    val description: String? = null,
    val difficulty: DifficultyLevel? = null,
    val creatorId: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val maxParticipants: Int? = null,
    val tags: List<String>? = null,
    val isActive: Boolean = true
)