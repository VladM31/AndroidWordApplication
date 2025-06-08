package can.lucky.of.history.domain.models.states

import can.lucky.of.history.domain.models.data.StatisticsLearningHistory
import java.time.LocalDate

internal data class StatisticLearningHistoryState(
    val toDate: LocalDate,
    val statistic: List<StatisticsLearningHistory> = emptyList()
)
