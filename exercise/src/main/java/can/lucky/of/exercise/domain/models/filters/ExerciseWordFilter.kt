package can.lucky.of.exercise.domain.models.filters

data class ExerciseWordFilter(
    val transactionId: String,
    val wordId: String? = null,
)
