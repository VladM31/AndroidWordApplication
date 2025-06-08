package can.lucky.of.history.domain.vms

import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.models.filters.Range
import can.lucky.of.core.domain.vms.AbstractMviViewModel
import can.lucky.of.history.domain.actions.StatisticLearningHistoryAction
import can.lucky.of.history.domain.managers.LearningHistoryManager
import can.lucky.of.history.domain.models.filters.StatisticsLearningHistoryFilter
import can.lucky.of.history.domain.models.states.StatisticLearningHistoryState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

internal class StatisticLearningHistoryVm(
    private val manager: LearningHistoryManager
) : AbstractMviViewModel<StatisticLearningHistoryState, StatisticLearningHistoryAction>() {

    private val mutableState = MutableStateFlow(
        StatisticLearningHistoryState(
            toDate = LocalDate.now()
        )
    )
    override val state: StateFlow<StatisticLearningHistoryState> = mutableState

    init {
        fetch(state.value.toDate)
    }



    override fun sent(action: StatisticLearningHistoryAction) {
        when (action) {
            is StatisticLearningHistoryAction.SetDate -> {
                fetch(action.date)
            }
        }
    }

    private fun fetch(date: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val statistic = manager.getLearningHistoryStatistic(
                filter = StatisticsLearningHistoryFilter(
                    date = Range(
                        from = date.minusDays(STEP).toString(),
                        to =date.toString()
                    )
                )
            )
            mutableState.value = mutableState.value.copy(statistic = statistic, toDate = date)
        }
    }

    companion object{
        internal const val STEP = 4L
    }
}