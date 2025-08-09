package can.lucky.of.addword.domain.models

import can.lucky.of.addword.domain.models.enums.RecognizeStatus
import can.lucky.of.core.domain.models.enums.Language
import java.time.LocalDateTime

data class AiRecognizeResult(
    val status: RecognizeStatus,
    val notice: String?,
    val recognizeId: String,
    val word: String?,
    val translation: String?,
    val language: Language,
    val translationLanguage: Language,
    val createdAt: LocalDateTime
)

