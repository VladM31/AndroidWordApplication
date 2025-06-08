package can.lucky.of.exercise.domain.models.data

data class ExerciseWord(
    val userWordId: String,
    val transactionId: String,
    val grade: Int = 0,
)