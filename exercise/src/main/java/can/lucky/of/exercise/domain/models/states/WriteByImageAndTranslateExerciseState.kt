package can.lucky.of.exercise.domain.models.states

import can.lucky.of.core.domain.models.enums.Exercise
import can.lucky.of.core.domain.models.states.EndetableState
import can.lucky.of.exercise.domain.models.data.ExerciseWordDetails

internal data class WriteByImageAndTranslateExerciseState(
    val words: List<ExerciseWordDetails> = emptyList(),
    val wordIndex: Int = 0,
    val isConfirm: Boolean? = null,
    val wordText: String? = null,
    val isEditEnable: Boolean = true,
    val transactionId: String = "",
    override val isEnd: Boolean = false,
    val isInited: Boolean = false,
    val isActiveSubscribe: Boolean = false,
    val mistakeCount : Int = 0,
    val grades: List<Int> = emptyList(),
    val exercise: Exercise = Exercise.WORD_BY_WRITE_TRANSLATE
) : EndetableState{
    fun currentWord() = words[wordIndex]
}