package can.lucky.of.exercise.domain.models.data

import java.time.LocalDateTime
import java.util.UUID

data class ExerciseTransaction(
    val id: String = UUID.randomUUID().toString(),
    val dateOfString: String = LocalDateTime.now().toString(),
    val dateOfEnd: String? = null,
    val isExecuted: Boolean = false,
    val isSynchronized: Boolean = false,
    val exercises: List<ExerciseDetails> = emptyList()
)