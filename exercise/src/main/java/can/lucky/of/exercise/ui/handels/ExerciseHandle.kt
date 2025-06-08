package can.lucky.of.exercise.ui.handels

import can.lucky.of.exercise.domain.actions.ExerciseAction
import can.lucky.of.exercise.domain.models.states.ExerciseState

interface ExerciseHandle {

    val state: ExerciseState

    fun sent(action: ExerciseAction)
}