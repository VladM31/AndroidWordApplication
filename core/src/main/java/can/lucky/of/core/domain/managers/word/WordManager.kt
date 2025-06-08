package can.lucky.of.core.domain.managers.word

import can.lucky.of.core.domain.models.data.words.InsertWord
import can.lucky.of.core.domain.models.data.words.PinUserWord
import can.lucky.of.core.domain.models.data.words.Word
import can.lucky.of.core.domain.models.filters.WordFilter


interface WordManager {
    suspend fun findBy(filter: WordFilter) : List<Word>

    suspend fun insert(words: Collection<InsertWord>) : IntArray

    suspend fun findOne(filter: WordFilter) : Word?

    suspend fun pin(words: Collection<PinUserWord>) : IntArray
}