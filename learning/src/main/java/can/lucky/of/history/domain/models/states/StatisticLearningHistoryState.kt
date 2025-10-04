package can.lucky.of.history.domain.models.states

import can.lucky.of.history.domain.models.data.StatisticsLearningHistory
import java.time.OffsetDateTime

internal data class StatisticLearningHistoryState(
    val toDate: OffsetDateTime,
    val statistic: List<StatisticsLearningHistory> = emptyList()
)
