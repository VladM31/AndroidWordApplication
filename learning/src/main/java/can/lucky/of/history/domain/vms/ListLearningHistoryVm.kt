package can.lucky.of.history.domain.vms

import androidx.paging.Pager
import androidx.paging.PagingConfig
import can.lucky.of.core.domain.vms.AbstractMviViewModel
import can.lucky.of.history.domain.actions.ListLearningHistoryAction
import can.lucky.of.history.domain.managers.LearningHistoryManager
import can.lucky.of.history.domain.models.data.LearningHistory
import can.lucky.of.history.domain.models.filters.LearningHistoryFilter
import can.lucky.of.history.domain.models.states.ListLearningHistoryState
import can.lucky.of.history.ui.source.LearningHistoryPageLoader
import can.lucky.of.history.ui.source.LearningHistoryPagingSource
import kotlinx.coroutines.flow.MutableStateFlow

internal class ListLearningHistoryVm(
    private val learningHistoryManager: LearningHistoryManager
) : AbstractMviViewModel<ListLearningHistoryState, ListLearningHistoryAction>() {

    private val mutableState =
        MutableStateFlow(ListLearningHistoryState(content = getPageHistory(LearningHistoryFilter())))
    override val state: MutableStateFlow<ListLearningHistoryState> = mutableState

    override fun sent(action: ListLearningHistoryAction) {
        when (action) {
            is ListLearningHistoryAction.UpdateFilter -> {
                handleUpdateFilter(action)
            }
        }
    }

    private fun handleUpdateFilter(action: ListLearningHistoryAction.UpdateFilter) {
        mutableState.value = mutableState.value.copy(
            filter = action.filter,
            content = getPageHistory(action.filter)
        )
    }

    private fun getPageHistory(filter: LearningHistoryFilter): Pager<Long, LearningHistory> {
        val loafer: LearningHistoryPageLoader = { page, pageSize ->
            try {
                learningHistoryManager.getLearningHistory(
                    filter.copy(
                        page = page,
                        size = pageSize
                    )
                ).content
            } catch (e: Exception) {
                emptyList()
            }
        }

        return Pager(
            config = PagingConfig(
                pageSize = HISTORY_SIZE,
                initialLoadSize = HISTORY_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { LearningHistoryPagingSource(loafer, pageSize = HISTORY_SIZE) }
        )
    }

    companion object {
        private const val HISTORY_SIZE = 30
    }

}