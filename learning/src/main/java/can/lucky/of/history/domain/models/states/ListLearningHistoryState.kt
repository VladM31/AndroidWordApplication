package can.lucky.of.history.domain.models.states

import androidx.paging.Pager
import can.lucky.of.history.domain.models.data.LearningHistory
import can.lucky.of.history.domain.models.filters.LearningHistoryFilter

internal data class ListLearningHistoryState(
    val content: Pager<Long, LearningHistory>,
    val filter: LearningHistoryFilter = LearningHistoryFilter()
)
