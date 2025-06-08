package can.lucky.of.exercise.domain.managers

import can.lucky.of.exercise.domain.models.data.ExerciseTransaction
import can.lucky.of.exercise.domain.models.filters.ExerciseTransactionFilter

interface ExerciseTransactionManager {

    suspend fun findBy(filter: ExerciseTransactionFilter) : ExerciseTransaction?

    suspend fun save(exerciseTransaction: ExerciseTransaction)

    suspend fun update(exerciseTransaction: ExerciseTransaction)

    suspend fun delete(id: String)
}