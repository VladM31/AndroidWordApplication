package can.lucky.of.exercise.ui.controllers

import can.lucky.of.core.domain.models.enums.Exercise
import can.lucky.of.core.domain.vms.MviViewModel
import can.lucky.of.exercise.domain.actions.ExerciseSelectAction
import can.lucky.of.exercise.domain.models.data.ExerciseSelection
import can.lucky.of.exercise.domain.models.states.ExerciseSelectState

internal class ExerciseSelectionController(
    private val viewModel: MviViewModel<ExerciseSelectState, ExerciseSelectAction>
) {

    fun isSelected(exercise: Exercise): Boolean {
        return viewModel.state.value.selectedExercises.containsKey(exercise)
    }

    fun select(exercise: Exercise) {
        viewModel.sent(ExerciseSelectAction.AddExercise(exercise))
    }

    fun unselect(exercise: Exercise) {
        viewModel.sent(ExerciseSelectAction.RemoveExercise(exercise))
    }

    val exercises: List<ExerciseSelection> get() = viewModel.state.value.exercises
}