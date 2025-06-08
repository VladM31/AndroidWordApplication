package can.lucky.of.exercise.db.entities

import androidx.room.Entity

@Entity(tableName = "exercises", primaryKeys = ["transactionId", "position"])
internal data class ExerciseEntity(
    val transactionId: String,
    val position: Int,
    val name: String,
    val isExecuted: Boolean,
)
