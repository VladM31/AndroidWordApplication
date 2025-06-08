package can.lucky.of.addword.net.models.requests


internal data class RecognizeWordByTextRequest(
    val original: String?,
    val translate: String?,
    val language: String,
    val translationLanguage: String
)
