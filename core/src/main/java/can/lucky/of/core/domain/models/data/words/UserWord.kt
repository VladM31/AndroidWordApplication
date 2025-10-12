package can.lucky.of.core.domain.models.data.words

import java.time.OffsetDateTime

data class UserWord(
    val id: String,
    val learningGrade: Long,
    val createdAt: OffsetDateTime,
    val lastReadDate: OffsetDateTime,
    val word: Word,
)
