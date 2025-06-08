package can.lucky.of.exercise.domain.models.states

import can.lucky.of.core.domain.models.enums.Exercise
import can.lucky.of.core.domain.models.states.EndetableState
import can.lucky.of.exercise.domain.models.data.ExerciseWordDetails

data class SelectingAnOptionState(
    val wordIndex: Int = 0,
    val words: List<ExerciseWordDetails> = emptyList(),
    val wordsToChoose: List<ExerciseWordDetails> = emptyList(),
    val isInited: Boolean = false,
    val isActiveSubscribe: Boolean = false,
    override val isEnd: Boolean = false,
    val waitNext: Boolean = false,
    val isCorrect: Boolean? = null,
    val grades: List<Int> = emptyList(),
    val exercise: Exercise = Exercise.WORD_BY_ORIGINALS,
    val transactionId: String = "",
    val isSoundBeforeAnswer: Boolean = false,
    val isSoundAfterAnswer: Boolean = false
) : EndetableState{
    fun currentWord() = words[wordIndex]
}
