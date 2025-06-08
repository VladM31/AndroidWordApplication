package can.lucky.of.core.domain.models.data.words

data class UserWord(
    val id: String,
    val learningGrade: Long,
    val dateOfAdded: String,
    val lastReadDate: String,
    val word: Word,
)
