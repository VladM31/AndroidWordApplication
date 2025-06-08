package can.lucky.of.exercise.domain.managers.impls

import can.lucky.of.exercise.db.dao.ExerciseWordDao
import can.lucky.of.exercise.db.entities.ExerciseWordEntity
import can.lucky.of.core.domain.managers.userwords.UserWordManager
import can.lucky.of.exercise.domain.models.data.ExerciseWord
import can.lucky.of.exercise.domain.models.data.ExerciseWordDetails
import can.lucky.of.exercise.domain.models.filters.ExerciseWordFilter
import can.lucky.of.core.domain.models.filters.UserWordFilter
import can.lucky.of.core.domain.models.data.words.UserWord
import can.lucky.of.exercise.domain.managers.ExerciseWordManager

internal class ExerciseWordManagerImpl(
    private val exerciseWordDao: ExerciseWordDao,
    private val userWordManager: UserWordManager,
) : ExerciseWordManager {
    override suspend fun findDetailsBy(filter: ExerciseWordFilter): List<ExerciseWordDetails> {
        val entities = findEntitiesBy(filter)

        val ids = entities.map { it.userWordId }

        val idMap = userWordManager.findBy(UserWordFilter(ids = ids)).let {
            mutableMapOf<String, UserWord>().apply {
                it.forEach { put(it.id, it) }
            }
        }

        val result = mutableListOf<ExerciseWordDetails>()

        entities.forEach {
            idMap[it.userWordId]?.let { userWord ->
                result.add(userWord.toDetails(it.transactionId, it.grade))
            }
        }

        return result
    }

    override suspend fun findBy(filter: ExerciseWordFilter): List<ExerciseWord> {
        return findEntitiesBy(filter).map {
            ExerciseWord(
                userWordId = it.userWordId,
                transactionId = it.transactionId,
                grade = it.grade
            )
        }
    }

    override suspend fun save(exerciseWords: List<ExerciseWord>) {
        exerciseWordDao.insertAll(exerciseWords.map { it.toEntity()})
    }

    override suspend fun update(exerciseWord: ExerciseWord) {
        exerciseWordDao.update(exerciseWord.toEntity())
    }

    override suspend fun updateAll(exerciseWords: List<ExerciseWord>) {
        exerciseWordDao.updateAll(exerciseWords.map { it.toEntity() })
    }

    private suspend fun findEntitiesBy(filter: ExerciseWordFilter) : List<ExerciseWordEntity> {
        return if (filter.wordId != null) {
            exerciseWordDao.findByTransactionIdAndWordId(filter.transactionId, filter.wordId)?.let {
                listOf(it)
            } ?: emptyList()
        } else {
            exerciseWordDao.findByTransactionId(filter.transactionId)
        }
    }

    private fun UserWord.toDetails(transactionId: String, grade: Int) = ExerciseWordDetails(
        transactionId = transactionId,
        grade = grade,
        userWordId = id,
        wordId = word.id,
        dateOfAdded = dateOfAdded,
        lastReadDate = lastReadDate,
        original = word.original,
        translate = word.translate,
        lang = word.lang,
        translateLang = word.translateLang,
        cefr = word.cefr,
        description = word.description,
        category = word.category,
        soundLink = word.soundLink,
        imageLink = word.imageLink
    )

    private fun ExerciseWord.toEntity() = ExerciseWordEntity(
        userWordId = userWordId,
        transactionId = transactionId,
        grade = grade
    )
}