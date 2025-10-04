package can.lucky.of.core.domain.managers.word

import can.lucky.of.core.domain.models.PagedModels
import can.lucky.of.core.domain.models.data.words.Word
import can.lucky.of.core.domain.models.filters.WordFilter


interface WordManager {
    suspend fun findBy(filter: WordFilter): PagedModels<Word>

    suspend fun findOne(filter: WordFilter) : Word?

}