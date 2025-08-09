package can.lucky.of.addword.net.models.requests

import can.lucky.of.core.domain.models.enums.Language


internal data class AiRecognizeWordByTextRequest(
    val original: String? ,
    val translate: String?,
    val language: Language,
    val translationLanguage: Language
)
