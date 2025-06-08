package can.lucky.of.exercise.domain.vm

import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.vms.AbstractMviViewModel
import can.lucky.of.exercise.domain.actions.LettersMatchAction
import can.lucky.of.exercise.domain.managers.ExerciseStatisticalManager
import can.lucky.of.exercise.domain.mappers.toWordCompleted
import can.lucky.of.exercise.domain.models.states.LettersMatchState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class LettersMatchVm(
    private val exerciseStatisticalManager : ExerciseStatisticalManager
) : AbstractMviViewModel<LettersMatchState, LettersMatchAction>() {


    private val mutableState = MutableStateFlow(LettersMatchState())
    override val state: StateFlow<LettersMatchState> = mutableState

    override fun sent(action: LettersMatchAction) {
        when (action) {
            is LettersMatchAction.Init -> handleInit(action)
            is LettersMatchAction.ClickOnLetter -> handleClickOnLetter(action)
            is LettersMatchAction.Next -> handleNext()
            is LettersMatchAction.PlusOneLetter -> handlePlusOneLetter()
            else -> {}
        }
    }

    private fun handleInit(action: LettersMatchAction.Init) {
        if (state.value.isInited) return
        val words = action.words.shuffled()

        mutableState.value = state.value.copy(
            words = words,
            isInited = true,
            isActiveSubscribe = action.isActiveSubscribe,
            originalWord = words[0].original,
            transactionId = action.transactionId,
            exerciseType = action.exerciseType,
            letters = words[0].original.map { LettersMatchState.Letter.from(it) }.shuffled()
        )
    }

    private fun handleClickOnLetter(action: LettersMatchAction.ClickOnLetter) {
        if (state.value.errorLetter != null) return

        val letter = state.value.currentLetterChar()
        if (action.letter != letter) {
            val errorLetter = LettersMatchState.ErrorLetter.from(action.letter, action.id)
            val grade = state.value.grade - 1

            mutableState.value = state.value.copy(
                errorLetter = errorLetter,
                attempts = state.value.attempts + 1,
                grade = if (grade < 0) 0 else grade
            )

            viewModelScope.launch {
                delay(1000)
                mutableState.value = state.value.copy(
                    errorLetter = null
                )
            }

            return
        }

        val newLetterIndex = state.value.letterIndex + 1
        val newResultWord = state.value.originalWord.substring(0, newLetterIndex)
        val isNext = newResultWord == state.value.originalWord
        val remoteLetter = LettersMatchState.Letter(action.letter, action.id)

        mutableState.value = state.value.copy(
            letterIndex = newLetterIndex,
            resultWord = newResultWord + if (isNext) "" else state.value.endLetter,
            letters = state.value.letters - remoteLetter,
            isNext = isNext
        )
    }

    private fun handleNext() {

        viewModelScope.launch(Dispatchers.IO){
            exerciseStatisticalManager.completeWord(state.value.toWordCompleted())
        }

        val newIndex = state.value.wordIndex + 1
        val isEnd = newIndex == state.value.words.size


        if (isEnd) {
            onEnd()
            return
        }

        val word = state.value.words[newIndex]
        mutableState.value = state.value.copy(
            wordIndex = newIndex,
            originalWord = word.original,
            resultWord = state.value.endLetter,
            letters = word.original.map { LettersMatchState.Letter.from(it) }.shuffled(),
            grades = state.value.let { it.grades + it.grade},
            letterIndex = 0,
            isNext = false,
            grade = 3,
            attempts = 0,
        )

    }

    private fun onEnd(){
        val grades = state.value.grades + state.value.grade

        mutableState.value = state.value.copy(
            isEnd = true,
            grades = grades
        )
    }

    private fun handlePlusOneLetter() {
        if (state.value.run { isInited.not() || isNext}) return

        val grade = state.value.grade - 1

        val letter = state.value.currentLetterChar()
        val plusLetter = state.value.letters.find { it.letter == letter } ?: return


        val newLetterIndex = state.value.letterIndex + 1
        val newResultWord = state.value.originalWord.substring(0, newLetterIndex)
        val isNext = newResultWord == state.value.originalWord

        mutableState.value = state.value.copy(
            grade = if (grade< 0) 0 else grade,
            letterIndex = newLetterIndex,
            resultWord = newResultWord + if (isNext) "" else state.value.endLetter,
            letters = state.value.letters - plusLetter,
            isNext = isNext
        )
    }

}