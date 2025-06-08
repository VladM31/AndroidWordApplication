package can.lucky.of.history.domain.actions

import can.lucky.of.history.domain.models.filters.LearningHistoryFilter

internal interface ListLearningHistoryAction {
     data class UpdateFilter(val filter: LearningHistoryFilter) : ListLearningHistoryAction
}