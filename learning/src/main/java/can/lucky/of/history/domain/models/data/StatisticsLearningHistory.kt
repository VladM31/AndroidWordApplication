package can.lucky.of.history.domain.models.data

import can.lucky.of.history.domain.models.enums.LearningHistoryType
import java.time.LocalDate
import java.util.Date

internal data class StatisticsLearningHistory(
    val count: Int,
    val grades: Long,
    val type: LearningHistoryType,
    val date: LocalDate
)
