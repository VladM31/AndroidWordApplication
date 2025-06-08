package can.lucky.of.addword.net.models.responses

import java.time.LocalDateTime


internal data class ShareUserWordResponse(
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