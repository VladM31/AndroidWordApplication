package can.lucky.of.exercise.domain.actions

import can.lucky.of.core.domain.models.enums.Exercise
import can.lucky.of.exercise.domain.models.data.ExerciseWordDetails

internal sealed interface WriteByImageAndTranslateExerciseAction {
    data class Init(
        val words: List<ExerciseWordDetails>,
        val transactionId: String,
        val exerciseType: Exercise,
        val isActiveSubscribe: Boolean = false
    ) : WriteByImageAndTranslateExerciseAction

    data class UpdateText(val text: String) : WriteByImageAndTranslateExerciseAction
    data object Confirm : WriteByImageAndTranslateExerciseAction
    data object NextWord : WriteByImageAndTranslateExerciseAction
    data object AddLetter : WriteByImageAndTranslateExerciseAction
}