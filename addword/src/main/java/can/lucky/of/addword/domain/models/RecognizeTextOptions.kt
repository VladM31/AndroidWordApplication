package can.lucky.of.addword.domain.models

import can.lucky.of.core.domain.models.enums.Language

internal data class RecognizeTextOptions(
    val original: String,
    val translate: String,
    val language: Language,
    val translationLanguage: Language
)
