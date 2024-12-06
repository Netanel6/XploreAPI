package org.netanel.quiz.repository.model
import kotlinx.serialization.Serializable
import quiz.repository.model.DifficultyLevel

@Serializable
data class QuestionV2(
    var id: Int? = null,
    val media: String? = null,
    val text: String? = null,
    val answers: List<String>? = null,
    val explanation: String? = null,
    val correctAnswerIndex: Int? = null,
    val points: Int,
    val type: QuestionType? = null,
    val difficulty: DifficultyLevel,
    val isMandatory: Boolean? = null
) {
    @Serializable
    enum class QuestionType {
        American, TrueOrFalse, FillInTheBlank
    }
}