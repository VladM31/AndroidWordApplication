package can.lucky.of.core.net.requests

data class LearningPlanRequest(
    val wordsPerDay: Int,
    val nativeLang: String,
    val learningLang: String,
    val cefr: String,
)