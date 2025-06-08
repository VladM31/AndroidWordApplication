package can.lucky.of.core.net.responses

data class LearningHistoryResponse(
    val id: String,
    val userId: String,
    val wordId: String,
    val original: String,
    val nativeLang: String,
    val learningLang: String,
    val cefr: String,
    val date: String,
    val type: String,
    val grade: Int,
)
