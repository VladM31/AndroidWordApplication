package can.lucky.of.exercise.domain.vm

import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.managers.media.MediaManager
import can.lucky.of.core.domain.vms.AbstractMviViewModel
import can.lucky.of.exercise.domain.actions.SelectingAnOptionAction
import can.lucky.of.exercise.domain.managers.ExerciseStatisticalManager
import can.lucky.of.exercise.domain.mappers.toWordCompleted
import can.lucky.of.exercise.domain.models.data.ExerciseWordDetails
import can.lucky.of.exercise.domain.models.states.SelectingAnOptionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class SelectingAnOptionVm(
    private val mediaManager: MediaManager,
    private val exerciseStatisticalManager: ExerciseStatisticalManager
) :
    AbstractMviViewModel<SelectingAnOptionState, SelectingAnOptionAction>() {


    private val mutableState = MutableStateFlow(SelectingAnOptionState())
    override val state: StateFlow<SelectingAnOptionState> = mutableState

    override fun sent(action: SelectingAnOptionAction) {
        when (action) {
            is SelectingAnOptionAction.Init -> handleInit(action)
            is SelectingAnOptionAction.ChooseWord -> handleChooseWord(action)
            is SelectingAnOptionAction.Next -> handleNext()
            else -> {}
        }
    }

    private fun handleChooseWord(action: SelectingAnOptionAction.ChooseWord) {
        val word = state.value.let { it.words[it.wordIndex] }
        val isCorrect = word.wordId == action.wordId

        val newIndex = state.value.wordIndex + 1
        val isEnd = newIndex == state.value.words.size

        state.value.let { it.words[it.wordIndex] }.soundLink?.let {link ->
            if (state.value.let { it.isActiveSubscribe && it.isSoundAfterAnswer }){
                mediaManager.load(link, playWhenReady = true)
            }
        }

        mutableState.value = state.value.copy(
            grades = state.value.grades + if (isCorrect) 1 else 0,
            isEnd = isEnd,
            waitNext = true,
            isCorrect = isCorrect
        )

        viewModelScope.launch(Dispatchers.IO){
            exerciseStatisticalManager.completeWord(
                state.value.toWordCompleted()
            )
        }
    }

    private fun handleNext(){
        val newIndex = state.value.wordIndex + 1
        val isEnd = newIndex == state.value.words.size

        state.value.words.getOrNull(newIndex)?.soundLink?.let {link ->
            if (state.value.let { it.isActiveSubscribe && it.isSoundBeforeAnswer }){
                mediaManager.load(link, playWhenReady = true)
            }
        }


        val wordsToChoose = if (isEnd) emptyList() else getChoiceWords(newIndex, state.value.words)
        mutableState.value = state.value.copy(
            wordIndex = if (isEnd) newIndex - 1 else newIndex,
            wordsToChoose = wordsToChoose,
            isEnd = isEnd,
            waitNext = false,
            isCorrect = null
        )
    }

    private fun handleInit(action: SelectingAnOptionAction.Init) {
        if (state.value.isInited) return
        val words = action.words.shuffled()


        words.firstOrNull()?.soundLink?.let { link ->
            if (action.let { it.isActiveSubscribe && it.isSoundBeforeAnswer }) {
                mediaManager.load(link, playWhenReady = true)
                mediaManager.start()
            }
        }

        mutableState.value = state.value.copy(
            isInited = true,
            words = words,
            isActiveSubscribe = action.isActiveSubscribe,
            wordsToChoose = getChoiceWords(state.value.wordIndex,words),
            exercise = action.exerciseType,
            transactionId = action.transactionId,
            isSoundBeforeAnswer = action.isSoundBeforeAnswer,
            isSoundAfterAnswer = action.isSoundAfterAnswer
        )
    }

    private fun getChoiceWords(index: Int, stateWords: List<ExerciseWordDetails>): List<ExerciseWordDetails> {
        val choiceWord = stateWords[index]
        val choiceWords = mutableSetOf<ExerciseWordDetails>()
        val words = stateWords.shuffled()

        choiceWords.add(choiceWord)

        for (word in words) {
            choiceWords.add(word)
            if (choiceWords.size == 3) {
                break
            }
        }

        return choiceWords.toList().shuffled()
    }
}