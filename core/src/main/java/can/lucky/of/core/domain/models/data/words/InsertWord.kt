package can.lucky.of.core.domain.models.data.words

import java.io.File

data class InsertWord(
    val word: String,
    val language: String,
    val translationLanguage: String,
    val translation: String? = null,
    val category: String? = null,
    val description: String? = null,
    val cefr: String? = null,
    var image: File? = null,
    var sound: File? = null,
    val needSound: Boolean = false,
)