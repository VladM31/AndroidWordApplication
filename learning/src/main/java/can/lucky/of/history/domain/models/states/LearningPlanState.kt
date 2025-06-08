package can.lucky.of.history.domain.models.states

import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.data.LearningPlan
import can.lucky.of.core.domain.models.states.ErrorableState
import can.lucky.of.history.domain.models.enums.PlanFragmentType

internal data class LearningPlanState(
    val type: PlanFragmentType = PlanFragmentType.UNDEFINED,
    val learningPlan: LearningPlan? = null,
    val learnedWordsToDay: Int = 1,
    val addedWords: Int = 0,
    val learnedWords: Int = 0,
    override val errorMessage: ErrorMessage? = null
) : ErrorableState
