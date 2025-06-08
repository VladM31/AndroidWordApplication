package can.lucky.of.exercise.domain.models.states

import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.enums.Exercise
import can.lucky.of.exercise.domain.models.data.ExerciseSelection

internal data class ExerciseSelectState(
    val exercises: List<ExerciseSelection> = Exercise.entries.map {
        ExerciseSelection(
            it
        )
    },
    val selectedExercises: Map<Exercise, Int> = mapOf(),
    var number: Int = 0,
    val transactionId: String = "",
    val errorMessage: ErrorMessage = ErrorMessage(),
    val isConfirmed: Boolean = false
)
