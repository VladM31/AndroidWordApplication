package can.lucky.of.core.domain.models.data

import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import java.time.OffsetDateTime

data class LearningPlan(
    val wordsPerDay :Int,
    val nativeLang : Language,
    val learningLang : Language,
    val cefr : CEFR,
    val createdAt: OffsetDateTime
)
