package can.lucky.of.core.net.responses

import java.time.LocalDateTime

data class LearningPlanResponse(
    val wordsPerDay: Int,
    val nativeLang: String,
    val learningLang: String,
    val cefr: String,
    val dateOfCreation: LocalDateTime
)
