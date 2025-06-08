package can.lucky.of.addword.domain.models

import java.time.LocalDateTime

data class ShareUserWord(
    val wordId: String,
    val original: String,
    val lang: String,
    val translate: String,
    val translateLang: String,
    val category: String?,
    val hasSound: Boolean,
    val hasImage: Boolean,
    val cefr: String,
    val description: String?,
    val expiredDate: LocalDateTime
)
