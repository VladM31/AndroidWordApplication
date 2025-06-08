package can.lucky.of.history.domain.vms

import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.managers.LearningPlanManager
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.data.LearningPlan
import can.lucky.of.core.domain.models.filters.Range
import can.lucky.of.core.domain.vms.AbstractMviViewModel
import can.lucky.of.history.domain.models.states.LearningPlanState
import can.lucky.of.history.domain.actions.LearningPlanAction
import can.lucky.of.history.domain.managers.LearningHistoryManager
import can.lucky.of.history.domain.models.data.CountLearningHistory
import can.lucky.of.history.domain.models.enums.PlanFragmentType
import can.lucky.of.history.domain.models.enums.LearningHistoryType
import can.lucky.of.history.domain.models.filters.LearningHistoryFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

internal class LearningPlanVm(
    private val learningHistoryManager: LearningHistoryManager,
    private val learningPlanManager: LearningPlanManager
) : AbstractMviViewModel<LearningPlanState, LearningPlanAction>() {

    private val mutableState = MutableStateFlow(LearningPlanState())
    override val state: StateFlow<LearningPlanState> = mutableState

    init {
        fetch()
    }

    override fun sent(action: LearningPlanAction) {
        when (action) {
            is LearningPlanAction.ReFetchPlan -> fetch()
        }
    }

    private fun fetch() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val plane = learningPlanManager.fetchLearningPlan()
                if (plane == null) {
                    mutableState.value = mutableState.value.copy(type = PlanFragmentType.CREATE)
                    return@launch
                }

                val learnedWordsToDay = toLearnedWordsToDay(plane)

                val countMap = learningHistoryManager.getCount().toMap()


                mutableState.value = state.value.copy(
                    type = PlanFragmentType.EDIT,
                    learningPlan = plane,
                    learnedWordsToDay = learnedWordsToDay,
                    addedWords = countMap[LearningHistoryType.CREATE] ?: 0,
                    learnedWords = countMap[LearningHistoryType.UPDATE] ?: 0
                )
            } catch (e: Exception) {
                mutableState.value =
                    mutableState.value.copy(errorMessage = ErrorMessage(message = e.message.orEmpty()))
            }
        }
    }

    private suspend fun toLearnedWordsToDay(plan: LearningPlan): Int {
        val historyFilter = LearningHistoryFilter(date = Range.of(LocalDate.now().toString()));

        val typeGroup = learningHistoryManager.getLearningHistory(historyFilter).filter {
            it.cefr == plan.cefr && it.learningLang == plan.learningLang && it.nativeLang == plan.nativeLang
        }.groupBy { it.type }

        val addedToday = typeGroup[LearningHistoryType.CREATE] ?: return 0
        val learnedToday = typeGroup[LearningHistoryType.UPDATE]?.groupBy { it.wordId } ?: return 0

        return addedToday.count { learnedToday.containsKey(it.wordId) }
    }

    private fun List<CountLearningHistory>.toMap(): Map<LearningHistoryType, Int> {
        val map = mutableMapOf<LearningHistoryType, Int>()

        forEach { map[LearningHistoryType.valueOf(it.type)] = it.count }

        return map
    }
}