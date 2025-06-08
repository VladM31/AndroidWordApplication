package can.lucky.of.exercise.domain.models.data

import java.time.Instant

data class EndExerciseTransaction(
    val transactionId: String,
    var endedAt: Long = Instant.now().toEpochMilli()
)