package can.lucky.of.exercise.domain.models.states

import can.lucky.of.core.domain.models.enums.Exercise
import can.lucky.of.core.domain.models.states.EndetableState
import can.lucky.of.exercise.domain.models.data.ExerciseWordDetails
import java.util.UUID

data class LettersMatchState(
    val endLetter: String = "⏎",
    val letterIndex: Int = 0,
    val originalWord: String = "",
    val resultWord: String = endLetter,
    val letters: List<Letter> = emptyList(),
    val isNext: Boolean = false,
    val grade: Int = 3,
    val attempts: Int = 0,
    val errorLetter: ErrorLetter? = null,
    val grades: List<Int> = emptyList(),

    val transactionId: String = "",
    val exerciseType: Exercise = Exercise.LETTERS_MATCH_BY_TRANSLATION,

    val wordIndex: Int = 0,
    val words: List<ExerciseWordDetails> = emptyList(),
    val isActiveSubscribe: Boolean = false,
    val isInited: Boolean = false,
    override val isEnd: Boolean = false,
) : EndetableState{

    fun currentLetterChar() = originalWord[letterIndex]

    fun currentWord() = words[wordIndex]

    data class Letter(
        val letter: Char,
        val id: String,
    ){
        companion object{
            fun from(letter: Char) = Letter(letter, UUID.randomUUID().toString())
        }
    }

    data class ErrorLetter(
        val letter: Letter
    ){

        companion object{
            fun from(letter: Char, id: String) = ErrorLetter(Letter(letter,id))
        }
    }
}
