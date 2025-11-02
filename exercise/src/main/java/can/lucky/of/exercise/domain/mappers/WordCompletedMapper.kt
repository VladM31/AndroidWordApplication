package can.lucky.of.exercise.domain.mappers

import can.lucky.of.core.domain.models.enums.Exercise
import can.lucky.of.exercise.domain.models.data.WordCompleted
import can.lucky.of.exercise.domain.models.states.CompareExerciseState
import can.lucky.of.exercise.domain.models.states.LettersMatchState
import can.lucky.of.exercise.domain.models.states.SelectingAnOptionState
import can.lucky.of.exercise.domain.models.states.WriteByImageAndFieldState
import java.time.Instant

internal fun LettersMatchState.toWordCompleted() : WordCompleted {
    val currentWord = this.currentWord()


    return WordCompleted(
        transactionId = transactionId,
        wordId = currentWord.wordId,
        userWordId = currentWord.userWordId,
        exerciseId = this.exerciseType.id,
        attempts = this.attempts,
        isCorrect = attempts < 3,
        completedAt = Instant.now().toEpochMilli()
    )
}

internal fun CompareExerciseState.toWordCompleted()  : WordCompleted {
    val currentWord = originalWords[original?.index ?: 0].word

    return WordCompleted(
        transactionId = transactionId,
        wordId = currentWord.wordId,
        userWordId = currentWord.userWordId,
        exerciseId = Exercise.COMPARE.id,
        attempts = this.attempts,
        isCorrect = attempts < 3,
        completedAt = Instant.now().toEpochMilli()
    )
}

internal fun SelectingAnOptionState.toWordCompleted()  : WordCompleted{
    return WordCompleted(
        transactionId = transactionId,
        wordId = currentWord().wordId,
        userWordId = currentWord().userWordId,
        exerciseId =  exercise.id,
        attempts = 0,
        isCorrect = isCorrect ?: false,
        completedAt = Instant.now().toEpochMilli()
    )
}

internal fun WriteByImageAndFieldState.toWordCompleted(): WordCompleted {
    val currentWord = this.currentWord()

    return WordCompleted(
        transactionId = transactionId,
        wordId = currentWord.wordId,
        userWordId = currentWord.userWordId,
        exerciseId = this.exercise.id,
        attempts = this.mistakeCount,
        isCorrect = mistakeCount < 3,
        completedAt = Instant.now().toEpochMilli()
    )
}

