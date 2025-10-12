package can.lucky.of.core.net.responses

import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.enums.LearningHistoryType

data class LearningHistoryResponse(
    val id: String,
    val userId: String,
    val wordId: String,
    val original: String,
    val nativeLang: Language,
    val learningLang: Language,
    val cefr: CEFR,
    val date: String,
    val type: LearningHistoryType,
    val grade: Int,
)
