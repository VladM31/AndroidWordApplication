package can.lucky.of.exercise.net.requests

data class EndExerciseTransactionRequest(
    val transactionId: String,
    var endedAt: Long
)