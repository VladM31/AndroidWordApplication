package can.lucky.of.exercise.domain.managers

import can.lucky.of.exercise.domain.models.data.ExerciseWord
import can.lucky.of.exercise.domain.models.data.ExerciseWordDetails
import can.lucky.of.exercise.domain.models.filters.ExerciseWordFilter

interface ExerciseWordManager {

    suspend fun findDetailsBy(filter: ExerciseWordFilter) : List<ExerciseWordDetails>

    suspend fun findBy(filter: ExerciseWordFilter) : List<ExerciseWord>

    suspend fun save(exerciseWords: List<ExerciseWord>)

    suspend fun update(exerciseWord: ExerciseWord)

    suspend fun updateAll(exerciseWords: List<ExerciseWord>)

}