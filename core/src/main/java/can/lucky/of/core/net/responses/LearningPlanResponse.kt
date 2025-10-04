package can.lucky.of.core.net.responses

import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import java.time.OffsetDateTime

data class LearningPlanResponse(
    val wordsPerDay: Int,
    val nativeLang: Language,
    val learningLang: Language,
    val cefr: CEFR,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)
