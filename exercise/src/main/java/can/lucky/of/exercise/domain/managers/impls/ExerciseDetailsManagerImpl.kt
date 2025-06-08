package can.lucky.of.exercise.domain.managers.impls

import can.lucky.of.exercise.db.dao.ExerciseDao
import can.lucky.of.exercise.db.entities.ExerciseEntity
import can.lucky.of.exercise.domain.managers.ExerciseDetailsManager
import can.lucky.of.exercise.domain.models.data.ExerciseDetails
import can.lucky.of.exercise.domain.models.filters.ExerciseDetailsFilter

internal class ExerciseDetailsManagerImpl(
    private val exerciseDao: ExerciseDao
) : ExerciseDetailsManager {
    override suspend fun findBy(filter: ExerciseDetailsFilter): List<ExerciseDetails> {
        return exerciseDao.findById(filter.id).map { it.toModel() }
    }

    override suspend fun saveAll(exercises: List<ExerciseDetails>) {
        exerciseDao.insertAll(exercises.map { it.toEntity() })
    }

    override suspend fun update(exercise: ExerciseDetails) {
        exerciseDao.update(exercise.toEntity())
    }

    private fun ExerciseDetails.toEntity() = ExerciseEntity(
        transactionId = transactionId,
        position = position,
        name = name,
        isExecuted = isExecuted
    )

    private fun ExerciseEntity.toModel() = ExerciseDetails(
        transactionId = transactionId,
        position = position,
        name = name,
        isExecuted = isExecuted
    )
}