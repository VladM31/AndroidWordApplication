package can.lucky.of.exercise.domain.actions

import can.lucky.of.core.domain.models.enums.Exercise
import can.lucky.of.exercise.domain.models.data.ExerciseWordDetails

interface LettersMatchAction {

    data class Init(
        val words: List<ExerciseWordDetails>,
        val isActiveSubscribe: Boolean,
        val transactionId: String,
        val exerciseType: Exercise
    ) : LettersMatchAction

    data class ClickOnLetter(val letter: Char, val id: String) : LettersMatchAction

    data object Next : LettersMatchAction

    data object PlusOneLetter : LettersMatchAction
}