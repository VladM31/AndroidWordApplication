package can.lucky.of.exercise.domain.actions

import can.lucky.of.exercise.domain.models.data.ExerciseWord


sealed interface ExerciseAction {
    data class Init(val transactionId: String, val playListId: String?) : ExerciseAction
    data class NextExercise(val words: List<ExerciseWord>) : ExerciseAction
}