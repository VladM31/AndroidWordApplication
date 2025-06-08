package can.lucky.of.history.domain.actions

import java.time.LocalDate

sealed interface StatisticLearningHistoryAction {
    data class SetDate(val date: LocalDate) : StatisticLearningHistoryAction
}