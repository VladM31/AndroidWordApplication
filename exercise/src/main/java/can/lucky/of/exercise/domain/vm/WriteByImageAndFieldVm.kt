package can.lucky.of.exercise.domain.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.vms.MviViewModel
import can.lucky.of.exercise.domain.actions.WriteByImageAndFieldAction
import can.lucky.of.exercise.domain.managers.ExerciseStatisticalManager
import can.lucky.of.exercise.domain.mappers.toWordCompleted
import can.lucky.of.exercise.domain.models.states.WriteByImageAndFieldState
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class WriteByImageAndFieldVm(
    private val exerciseStatisticalManager: ExerciseStatisticalManager
) : ViewModel(),
    MviViewModel<WriteByImageAndFieldState, WriteByImageAndFieldAction> {

    private val mutableState = MutableStateFlow(WriteByImageAndFieldState())
    override val state: StateFlow<WriteByImageAndFieldState>
        get() = mutableState

    override fun sent(action: WriteByImageAndFieldAction) {
        when(action){
            is WriteByImageAndFieldAction.Init -> handleInit(action)
            is WriteByImageAndFieldAction.UpdateText -> handleUpdateText(action)
            is WriteByImageAndFieldAction.Confirm -> handleConfirm()
            is WriteByImageAndFieldAction.NextWord -> handleNextWord()
            is WriteByImageAndFieldAction.AddLetter -> handleAddLetter()
        }
    }

    private fun handleAddLetter() {
        if (state.value.isEditEnable.not()) return

        val currentWord = state.value.currentWord()
        val textBuilder = StringBuilder()
        val textNow = state.value.wordText?.trim() ?: ""

        if (textNow.isEmpty()){
            mutableState.value = state.value.copy(wordText = currentWord.original.first().toString())
            return
        }

        val textNowList = textNow.toCharArray().toMutableList()
        val originalList = currentWord.original.toCharArray().toMutableList()

        for (i in 0 until originalList.size){
            textBuilder.append(originalList[i])
            if (originalList[i].isWhitespace()){
                continue
            }

            if (originalList[i] != textNowList.getOrNull(i)){
                break
            }
        }



        mutableState.value = state.value.copy(wordText = textBuilder.toString())
    }

    private fun handleInit(action: WriteByImageAndFieldAction.Init) {
        mutableState.value = mutableState.value.copy(
            words = action.words.shuffled(),
            isInited = true,
            isActiveSubscribe = action.isActiveSubscribe,
            transactionId = action.transactionId,
            exercise = action.exerciseType
        )
    }

    private fun handleUpdateText(action: WriteByImageAndFieldAction.UpdateText) {
        mutableState.value = mutableState.value.copy(wordText = action.text)
    }

    private fun handleConfirm(){
        val original = state.value.currentWord().original.lowercase().trim()
        val inputWord = state.value.wordText?.trim()?.lowercase()

        val isNotConfirm = original != inputWord

        if (isNotConfirm) {
            mutableState.value = mutableState.value.copy(
                isConfirm = false,
                isEditEnable = false,
                mistakeCount = state.value.mistakeCount + 1
            )

            viewModelScope.launch {
                delay(1000)
                mutableState.value = mutableState.value.copy(isEditEnable = true, isConfirm = null)
            }
            return
        }
        val grade = if (3 - state.value.mistakeCount > 0) 3 - state.value.mistakeCount else 0
        viewModelScope.launch(Dispatchers.IO) {
            exerciseStatisticalManager.completeWord(state.value.toWordCompleted())
        }

        mutableState.value = state.value.copy(
            isEditEnable = false,
            isConfirm = true,
            mistakeCount = 0,
            grades = state.value.grades + grade
        )
    }

    private fun handleNextWord(){
        val nextIndex = state.value.wordIndex + 1
        if (nextIndex >= state.value.words.size) {
            mutableState.value = state.value.copy(isEnd = true)
            return
        }
        mutableState.value = state.value.copy(
            wordIndex = nextIndex,
            wordText = null,
            isConfirm = null,
            isEditEnable = true
        )
    }

}