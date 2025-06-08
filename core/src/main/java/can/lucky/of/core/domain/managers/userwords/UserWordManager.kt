package can.lucky.of.core.domain.managers.userwords

import can.lucky.of.core.domain.models.data.words.DeleteUserWord
import can.lucky.of.core.domain.models.data.words.GradeUserWord
import can.lucky.of.core.domain.models.data.words.UserWord
import can.lucky.of.core.domain.models.filters.UserWordFilter


interface UserWordManager {

    suspend fun countBy(filter: UserWordFilter): Long

    suspend fun findBy(filter: UserWordFilter): List<UserWord>

    suspend fun putGrades(requests: List<GradeUserWord>): IntArray

    suspend fun delete(requests: List<DeleteUserWord>): IntArray
}