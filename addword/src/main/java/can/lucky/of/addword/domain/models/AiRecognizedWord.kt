package can.lucky.of.addword.domain.models

import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language

internal data class AiRecognizedWord(
    val recognizeId: String,
    val word: String,
    val translation: String,
    val description: String,
    val category: String,
    val cefr: CEFR,
    val language: Language,
    val translationLanguage: Language
)
