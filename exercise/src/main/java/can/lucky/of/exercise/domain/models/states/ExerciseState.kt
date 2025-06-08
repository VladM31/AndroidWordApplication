package can.lucky.of.exercise.domain.models.states

import can.lucky.of.core.domain.models.states.EndetableState
import can.lucky.of.exercise.domain.models.data.ExerciseDetails
import can.lucky.of.exercise.domain.models.data.ExerciseTransaction
import can.lucky.of.exercise.domain.models.data.ExerciseWordDetails

data class ExerciseState(
    val playListId: String? = null,
    val transactionId: String = "",
    val exerciseTransaction: ExerciseTransaction = ExerciseTransaction(),
    val exercises: List<ExerciseDetails> = emptyList(),
    val words: List<ExerciseWordDetails> = emptyList(),
    val isInited: Boolean = false,
    override val isEnd: Boolean = false,
    val isActiveSubscribe: Boolean = false,
) : EndetableState
