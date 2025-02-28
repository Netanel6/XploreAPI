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
    val quizTimer: Int,
    val answerLockTimer: Int,
    val title: String? = null,
    val creatorId: String? = null,
    val isActive: Boolean = true,
    var totalScore: Int = 0
)