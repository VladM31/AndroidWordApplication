package can.lucky.of.core.net.requests

import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language

data class LearningPlanRequest(
    val wordsPerDay: Int,
    val nativeLang: Language,
    val learningLang: Language,
    val cefr: CEFR,
)