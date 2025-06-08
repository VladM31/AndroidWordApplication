package can.lucky.of.exercise.db.entities

import androidx.room.Entity

@Entity(tableName = "exercise_words", primaryKeys = ["userWordId", "transactionId"])
internal data class ExerciseWordEntity(
    val userWordId: String,
    val transactionId: String,
    val grade: Int,
)