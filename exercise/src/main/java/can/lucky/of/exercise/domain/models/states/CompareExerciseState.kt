package can.lucky.of.exercise.domain.models.states

import can.lucky.of.core.domain.models.states.EndetableState
import can.lucky.of.exercise.domain.models.data.CompareWordBox
import can.lucky.of.exercise.domain.models.data.ExerciseWordDetails

internal data class CompareExerciseState(
    val originalWords: List<CompareWordBox> = emptyList(),
    val translateWords: List<CompareWordBox> = emptyList(),
    val words: List<ExerciseWordDetails> = emptyList(),
    val isInited: Boolean = false,
    val attempts: Int = 0,
    override val isEnd: Boolean = false,
    val original: WordBox? = null,
    val translate: WordBox? = null,
    val transactionId: String = "",
) : EndetableState{
    data class WordBox(
        val id: String,
        val index: Int,
    )
}