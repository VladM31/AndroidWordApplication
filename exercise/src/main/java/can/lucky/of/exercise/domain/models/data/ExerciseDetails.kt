package can.lucky.of.exercise.domain.models.data

data class ExerciseDetails(
    val transactionId: String,
    val position: Int,
    val name: String,
    val isExecuted: Boolean = false
)
