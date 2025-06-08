package can.lucky.of.addword.domain.models

import can.lucky.of.core.domain.models.enums.Language

internal data class WordTextOptions(
    val original: String,
    val translate: String,
    val wordLang: Language,
    val translateLang: Language
)
