package can.lucky.of.exercise.domain.managers

import can.lucky.of.exercise.domain.models.data.ExerciseDetails
import can.lucky.of.exercise.domain.models.filters.ExerciseDetailsFilter

interface ExerciseDetailsManager {

    suspend fun findBy(filter: ExerciseDetailsFilter) : List<ExerciseDetails>

    suspend fun saveAll(exercises: List<ExerciseDetails>)

    suspend fun update(exercise: ExerciseDetails)
}