package can.lucky.of.history.domain.models.data

import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.history.domain.models.enums.LearningHistoryType
import java.time.LocalDate
import java.util.Date

internal data class LearningHistory(
    val id: String,
    val wordId: String,
    val original: String,
    val nativeLang: Language,
    val learningLang: Language,
    val cefr: CEFR,
    val date: LocalDate,
    val type: LearningHistoryType,
    val grade: Int,
)
