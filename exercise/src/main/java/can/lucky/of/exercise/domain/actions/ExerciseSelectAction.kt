package can.lucky.of.exercise.domain.actions

import can.lucky.of.core.domain.models.enums.Exercise


internal sealed interface ExerciseSelectAction {
    data class AddExercise(val exercise: Exercise) : ExerciseSelectAction
    data class RemoveExercise(val exercise: Exercise) : ExerciseSelectAction
    data class SetTransactionId(val transactionId: String) : ExerciseSelectAction
    data object ConfirmSelection : ExerciseSelectAction
}