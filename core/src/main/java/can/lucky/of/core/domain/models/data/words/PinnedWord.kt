package can.lucky.of.core.domain.models.data.words

import java.time.OffsetDateTime


data class PinnedWord(
    val learningGrade: Long,
    val lastReadDate: OffsetDateTime,
    val userWord: UserWord
)
