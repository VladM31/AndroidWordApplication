package can.lucky.of.exercise.domain.models.data

data class ExerciseWordDetails(
    val userWordId: String,
    val wordId: String,
    val transactionId: String,
    val grade: Int,
    val dateOfAdded: String,
    val lastReadDate: String,
    val original: String,
    val translate: String,
    val lang: String,
    val translateLang: String,
    val cefr: String,
    val description: String?,
    val category: String?,
    val soundLink: String?,
    val imageLink: String?
)
