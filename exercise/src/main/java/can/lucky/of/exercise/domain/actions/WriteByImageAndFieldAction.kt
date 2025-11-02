package can.lucky.of.exercise.domain.actions

import can.lucky.of.core.domain.models.enums.Exercise
import can.lucky.of.exercise.domain.models.data.ExerciseWordDetails

internal sealed interface WriteByImageAndFieldAction {
    data class Init(
        val words: List<ExerciseWordDetails>,
        val transactionId: String,
        val exerciseType: Exercise,
        val isActiveSubscribe: Boolean = false
    ) : WriteByImageAndFieldAction

    data class UpdateText(val text: String) : WriteByImageAndFieldAction
    data object Confirm : WriteByImageAndFieldAction
    data object NextWord : WriteByImageAndFieldAction
    data object AddLetter : WriteByImageAndFieldAction
}