package can.lucky.of.addword.net.models.responses

internal data class RecognizeWordResponse(
    val word: String,
    val translate: String,
    val description: String,
    val category: String,
    val cefr: String
)
