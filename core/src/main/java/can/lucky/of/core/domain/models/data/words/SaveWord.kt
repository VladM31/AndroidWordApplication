package can.lucky.of.core.domain.models.data.words

import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import java.io.File

data class SaveWord(
    val word: String,
    val translation: String,
    val language: Language,
    val translationLanguage: Language,
    val cefr: CEFR,

    val category: String? = null,
    val description: String? = null,

    var image: File? = null,
    var sound: File? = null,
    val needSound: Boolean = false,
)