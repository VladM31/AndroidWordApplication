package can.lucky.of.history.domain.vms

import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.managers.LearningPlanManager
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.data.LearningPlan
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.vms.AbstractMviViewModel
import can.lucky.of.history.domain.actions.ChangeLearningPlanAction
import can.lucky.of.history.domain.managers.LearningHistoryManager
import can.lucky.of.history.domain.models.enums.PlanFragmentType
import can.lucky.of.history.domain.models.states.ChangeLearningPlanState
import can.lucky.of.validation.Validator
import can.lucky.of.validation.schemes.ValidationScheme
import can.lucky.of.validation.schemes.isDifferentLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.OffsetDateTime

internal class ChangeLearningPlanVm(
    private val learningPlanManager: LearningPlanManager
) : AbstractMviViewModel<ChangeLearningPlanState, ChangeLearningPlanAction>(){

    private val mutableState = MutableStateFlow(ChangeLearningPlanState())
    override val state: StateFlow<ChangeLearningPlanState> = mutableState
    private val validator = Validator(state)

    init {
        validator.add({ it }, ValidationScheme<ChangeLearningPlanState>("lang").isDifferentLanguage())
    }

    override fun sent(action: ChangeLearningPlanAction) {
        when (action) {
            is ChangeLearningPlanAction.Init -> handleInit(action)
            is ChangeLearningPlanAction.ChangeWordsPerDay -> handleChangeWordsPerDay(action.wordsPerDay)
            is ChangeLearningPlanAction.PlusOneWordsPerDay -> handlePlusOneWordsPerDay()
            is ChangeLearningPlanAction.MinusOneWordsPerDay -> handleMinusOneWordsPerDay()
            is ChangeLearningPlanAction.ChangeNativeLang -> {
                mutableState.value = mutableState.value.copy(nativeLang = Language.fromTitleCase(action.lang))
            }
            is ChangeLearningPlanAction.ChangeLearningLang -> {
                mutableState.value = mutableState.value.copy(learningLang = Language.fromTitleCase(action.lang))
            }
            is ChangeLearningPlanAction.ChangeCefr -> {
                mutableState.value = mutableState.value.copy(cefr = action.cefr)
            }
            is ChangeLearningPlanAction.Submit -> handleSubmit()
        }
    }

    private fun handleInit(action: ChangeLearningPlanAction.Init) {
        mutableState.value = mutableState.value.copy(
            isInit = true,
            type = action.bundle.type,
            wordsPerDay = action.bundle.wordsPerDay,
            nativeLang = action.bundle.nativeLang,
            learningLang = action.bundle.learningLang,
            cefr = action.bundle.cefr
        )
    }


    private fun handlePlusOneWordsPerDay() {
        val newValue = mutableState.value.wordsPerDay + 1

        if (newValue > 100) {
            return
        }

        mutableState.value = mutableState.value.copy(wordsPerDay = newValue)
    }

    private fun handleMinusOneWordsPerDay() {
        val newValue = mutableState.value.wordsPerDay - 1

        if (newValue < 1) {
            return
        }

        mutableState.value = mutableState.value.copy(wordsPerDay = newValue)
    }

    private fun handleChangeWordsPerDay(wordsPerDay: Int) {
        if (wordsPerDay < 1 || wordsPerDay > 100) {
            return
        }
        mutableState.value = mutableState.value.copy(wordsPerDay = wordsPerDay)
    }

    private fun handleSubmit() {
        if (state.value.isInit.not()) {
            return
        }

        val error = validator.validate()

        if (error.isNotBlank()) {
            mutableState.value = mutableState.value.copy(errorMessage = ErrorMessage(message = error))
            return
        }

        viewModelScope.launch(Dispatchers.IO){
            try {
                if (mutableState.value.type == PlanFragmentType.CREATE) {
                    learningPlanManager.createLearningPlan(mutableState.value.toPlan())
                } else {
                    learningPlanManager.updateLearningPlan(mutableState.value.toPlan())
                }
                mutableState.value = mutableState.value.copy(isEnd = true)
            }catch (e: Exception){
                mutableState.value = mutableState.value.copy(errorMessage = ErrorMessage(message = e.message.orEmpty()))
            }
        }
    }

    private fun ChangeLearningPlanState.toPlan() : LearningPlan {
        return LearningPlan(
            wordsPerDay = wordsPerDay,
            nativeLang = nativeLang,
            learningLang = learningLang,
            cefr = cefr,
            createdAt = OffsetDateTime.now()
        )
    }
}