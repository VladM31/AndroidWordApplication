package can.lucky.of.exercise.domain.mappers

import can.lucky.of.core.domain.models.enums.Exercise
import can.lucky.of.exercise.domain.models.data.ExerciseWordDetails
import can.lucky.of.exercise.domain.models.data.StartExerciseTransaction
import can.lucky.of.exercise.domain.models.states.ExerciseState
import java.time.Instant


fun ExerciseState.toStartExerciseTransaction(): StartExerciseTransaction {
    return StartExerciseTransaction(
        transactionId = this.transactionId,
        exercises = this.exercises.map { Exercise.valueOf(it.name).id },
        createdAt = Instant.now().toEpochMilli(),
        words = this.words.map { it.toWord() }
    )
}

private fun ExerciseWordDetails.toWord() : StartExerciseTransaction.Word {
    return StartExerciseTransaction.Word(
        wordId = this.wordId,
        userWordId = this.userWordId,
        grade = this.grade,
        dateOfAdded = this.dateOfAdded,
        lastReadDate = this.lastReadDate,
        original = this.original,
        translate = this.translate,
        lang = this.lang,
        translateLang = this.translateLang,
        cefr = this.cefr,
        hasDescription = this.description.isNullOrBlank().not(),
        category = this.category,
        hasSound = this.soundLink.isNullOrBlank().not(),
        hasImage = this.imageLink.isNullOrBlank().not()
    )
}