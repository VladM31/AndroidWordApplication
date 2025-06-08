package can.lucky.of.exercise.domain.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import can.lucky.of.exercise.domain.models.data.CompareWordBox
import can.lucky.of.exercise.domain.models.states.CompareExerciseState
import can.lucky.of.core.domain.vms.MviViewModel
import can.lucky.of.exercise.domain.actions.CompareExerciseAction
import can.lucky.of.exercise.domain.managers.ExerciseStatisticalManager
import can.lucky.of.exercise.domain.mappers.toWordCompleted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

internal class MatchExerciseVm(
    private val exerciseStatisticalManager : ExerciseStatisticalManager
) : ViewModel(), MviViewModel<CompareExerciseState, CompareExerciseAction> {

    private val positionKeeper = AtomicInteger(0)
    private val mutableState = MutableStateFlow(CompareExerciseState())
    override val state: StateFlow<CompareExerciseState> = mutableState

    override fun sent(action: CompareExerciseAction) {
        when (action) {
            is CompareExerciseAction.Init -> handleInit(action)
            is CompareExerciseAction.Click -> handleClick(action)

        }
    }

    private fun handleClick(action: CompareExerciseAction.Click){
        val wordBox = CompareExerciseState.WordBox(action.wordId, action.index)
        val newState = if (action.isOriginal){
            mutableState.value.copy(original = wordBox)
        } else {
            mutableState.value.copy(translate = wordBox)
        }

        if (newState.original == null || newState.translate == null){
            mutableState.value = newState
            return
        }

        val isMistake = newState.original.id != newState.translate.id
        val originalWords = newState.originalWords.toMutableList()
        val translateWords = newState.translateWords.toMutableList()

        if (isMistake.not()){
            val position = positionKeeper.getAndIncrement()
            originalWords[newState.original.index] = originalWords[newState.original.index].copy(position = position)
            translateWords[newState.translate.index] = translateWords[newState.translate.index].copy(position = position)

            val isEnd = position == newState.words.size - 1

            viewModelScope.launch(Dispatchers.IO) {
                exerciseStatisticalManager.completeWord(
                    newState.toWordCompleted()
                )
            }

            mutableState.value = newState.copy(
                original = null,
                translate = null,
                originalWords = originalWords.sorted(),
                translateWords = translateWords.sorted(),
                isEnd = isEnd,
                attempts = 0
            )
            return
        }

        viewModelScope.launch(Dispatchers.Main.immediate) {
            originalWords[newState.original.index] = originalWords[newState.original.index].copy(isMistake = true)
            translateWords[newState.translate.index] = translateWords[newState.translate.index].copy(isMistake = true)
            mutableState.value = newState.copy(
                originalWords = originalWords.toList(),
                translateWords = translateWords.toList()
            )
            delay(1000)
            originalWords[newState.original.index] = originalWords[newState.original.index].copy(isMistake = false)
            translateWords[newState.translate.index] = translateWords[newState.translate.index].copy(isMistake = false)
            mutableState.value = newState.copy(
                original = null,
                translate = null,
                attempts = newState.attempts + 1,
                originalWords = originalWords,
                translateWords = translateWords
            )
        }
    }

    private fun handleInit(action: CompareExerciseAction.Init) {
        val words = action.words.shuffled()
        mutableState.value = mutableState.value.copy(
            words = words,
            originalWords = words.map { CompareWordBox(it) }.shuffled(),
            translateWords = words.map { CompareWordBox(it) }.shuffled(),
            isInited = true,
            transactionId = action.transactionId
        )
    }
}