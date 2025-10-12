package can.lucky.of.core.domain.managers.userwords

import can.lucky.of.core.domain.models.PagedModels
import can.lucky.of.core.domain.models.data.words.DeleteUserWord
import can.lucky.of.core.domain.models.data.words.PinUserWord
import can.lucky.of.core.domain.models.data.words.SaveWord
import can.lucky.of.core.domain.models.data.words.UserWord
import can.lucky.of.core.domain.models.filters.UserWordFilter


interface UserWordManager {

    suspend fun findBy(filter: UserWordFilter): PagedModels<UserWord>

    suspend fun save(words: Collection<SaveWord>)

    suspend fun pin(pins: Collection<PinUserWord>)

    suspend fun delete(requests: List<DeleteUserWord>)
}