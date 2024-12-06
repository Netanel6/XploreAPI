package quiz.repository.model

import kotlinx.serialization.Serializable

@Serializable
    enum class DifficultyLevel {
        Easy, Medium, Hard
    }