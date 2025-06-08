package can.lucky.of.core.domain.models.data.words



data class PinnedWord(
    val learningGrade: Long,
    val lastReadDate: String,
    val userWord: UserWord
)
