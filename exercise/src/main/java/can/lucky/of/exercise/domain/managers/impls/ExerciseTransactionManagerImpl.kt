package can.lucky.of.exercise.domain.managers.impls

import can.lucky.of.exercise.db.dao.ExerciseTransactionDao
import can.lucky.of.exercise.db.entities.ExerciseTransactionEntity
import can.lucky.of.exercise.domain.managers.ExerciseDetailsManager
import can.lucky.of.exercise.domain.managers.ExerciseTransactionManager
import can.lucky.of.exercise.domain.models.data.ExerciseTransaction
import can.lucky.of.exercise.domain.models.filters.ExerciseDetailsFilter
import can.lucky.of.exercise.domain.models.filters.ExerciseTransactionFilter

internal class ExerciseTransactionManagerImpl(
    private val exerciseTransactionDao: ExerciseTransactionDao,
    private val exerciseManager: ExerciseDetailsManager
) : ExerciseTransactionManager {
    override suspend fun findBy(filter: ExerciseTransactionFilter): ExerciseTransaction? {
        return exerciseTransactionDao.findById(filter.id)?.let {
            ExerciseTransaction(
                id = it.id,
                dateOfString = it.dateOfString,
                dateOfEnd = it.dateOfEnd,
                isExecuted = it.isExecuted,
                isSynchronized = it.isSynchronized,
                exercises = exerciseManager.findBy(ExerciseDetailsFilter(it.id))
            )
        }
    }

    override suspend fun save(exerciseTransaction: ExerciseTransaction) {
        exerciseTransactionDao.insert(exerciseTransaction.toEntity())
    }

    override suspend fun update(exerciseTransaction: ExerciseTransaction) {
        exerciseTransactionDao.update(exerciseTransaction.toEntity())
    }

    override suspend fun delete(id: String) {
        exerciseTransactionDao.delete(id)
    }

   private fun ExerciseTransaction.toEntity() : ExerciseTransactionEntity{
        return ExerciseTransactionEntity(
            id = id,
            dateOfString = dateOfString,
            dateOfEnd = dateOfEnd,
            isExecuted = isExecuted,
            isSynchronized = isSynchronized
        )
    }
}