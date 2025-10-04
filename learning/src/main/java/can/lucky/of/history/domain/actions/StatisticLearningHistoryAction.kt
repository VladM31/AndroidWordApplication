package can.lucky.of.history.domain.actions

import java.time.OffsetDateTime

sealed interface StatisticLearningHistoryAction {
    data class SetDate(val date: OffsetDateTime) : StatisticLearningHistoryAction
}