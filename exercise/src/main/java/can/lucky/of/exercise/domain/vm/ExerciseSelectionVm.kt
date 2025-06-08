package can.lucky.of.exercise.domain.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.enums.Exercise
import can.lucky.of.core.domain.vms.MviViewModel
import can.lucky.of.exercise.domain.actions.ExerciseSelectAction
import can.lucky.of.exercise.domain.managers.ExerciseDetailsManager
import can.lucky.of.exercise.domain.models.data.ExerciseDetails
import can.lucky.of.exercise.domain.models.data.ExerciseSelection
import can.lucky.of.exercise.domain.models.states.ExerciseSelectState

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class ExerciseSelectionVm(
    private val exerciseDetailsManager: ExerciseDetailsManager
) : ViewModel(), MviViewModel<ExerciseSelectState, ExerciseSelectAction> {

    private val mutableState = MutableStateFlow(ExerciseSelectState())
    override val state: StateFlow<ExerciseSelectState> = mutableState

    override fun sent(action: ExerciseSelectAction){
        when(action){
            is ExerciseSelectAction.AddExercise -> addExercise(action.exercise)
            is ExerciseSelectAction.RemoveExercise -> removeExercise(action.exercise)
            is ExerciseSelectAction.SetTransactionId -> mutableState.value = state.value.copy(transactionId = action.transactionId)
            is ExerciseSelectAction.ConfirmSelection -> handleConfirmSelection()
        }
    }

    private fun addExercise(exercise: Exercise){
        if (state.value.selectedExercises.containsKey(exercise)){
            return
        }
        val newNumber = state.value.number + 1
        val exerciseSelectionIndexed = state.value.exercises.withIndex().first{ it.value.exercise == exercise }

        val newExercises = state.value.exercises.toMutableList().apply {
            removeAt(exerciseSelectionIndexed.index)
            add(
                ExerciseSelection(
                    exercise,
                    newNumber
                )
            )
        }.sorted()
        mutableState.value = state.value.copy(
            exercises = newExercises,
            selectedExercises = state.value.selectedExercises + (exercise to newNumber),
            number = newNumber
        )
    }

    private fun removeExercise(exercise: Exercise){
        val numberExercise = state.value.selectedExercises[exercise] ?: return
        val newNumber = state.value.number - 1
        val newSelectedExercises = state.value.selectedExercises.toMutableMap().apply {
            remove(exercise)
        }

        val newExercises = state.value.exercises.toMutableList().apply {
            removeAt(numberExercise - 1)
            add(ExerciseSelection(exercise))
        }

        for (i in 0 until newNumber){
            newExercises[i] = newExercises[i].copy(number = i + 1)
            newSelectedExercises[newExercises[i].exercise] = i + 1
        }

        mutableState.value = state.value.copy(
            exercises = newExercises.sorted(),
            selectedExercises = newSelectedExercises,
            number = newNumber
        )
    }

    private fun handleConfirmSelection() {
        if (state.value.isConfirmed) {
            return
        }

        if (state.value.number == 0) {
            mutableState.value = state.value.copy(
                errorMessage = ErrorMessage(message = "You must select at least one exercise")
            )
            return
        }

        viewModelScope.launch(Dispatchers.IO){
            val selectedExercises = state.value.selectedExercises.map {
                ExerciseDetails(
                    name = it.key.name,
                    position = it.value,
                    transactionId = state.value.transactionId
                )
            }

            exerciseDetailsManager.saveAll(selectedExercises)

            mutableState.value = state.value.copy(
                isConfirmed = true
            )
        }
    }
}