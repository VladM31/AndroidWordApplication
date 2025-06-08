package can.lucky.of.exercise.domain.actions

import can.lucky.of.exercise.domain.models.data.ExerciseWordDetails


internal sealed interface CompareExerciseAction {
    data class Init(val words: List<ExerciseWordDetails>, val transactionId: String) : CompareExerciseAction
    data class Click(val isOriginal: Boolean, val wordId: String, val index: Int) :
        CompareExerciseAction
}