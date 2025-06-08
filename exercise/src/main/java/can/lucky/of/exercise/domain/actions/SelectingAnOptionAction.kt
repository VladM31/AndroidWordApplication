package can.lucky.of.exercise.domain.actions

import can.lucky.of.core.domain.models.enums.Exercise
import can.lucky.of.exercise.domain.models.data.ExerciseWordDetails

interface SelectingAnOptionAction {
    data class Init(
        val words: List<ExerciseWordDetails>,
        val exerciseType : Exercise,
        val transactionId: String,
        val isActiveSubscribe: Boolean,
        val isSoundBeforeAnswer: Boolean,
        val isSoundAfterAnswer: Boolean
    ) : SelectingAnOptionAction
    data class ChooseWord(val wordId: String) : SelectingAnOptionAction
    data object Next : SelectingAnOptionAction
}