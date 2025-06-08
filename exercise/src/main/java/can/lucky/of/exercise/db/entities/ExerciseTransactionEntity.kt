package can.lucky.of.exercise.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_transactions")
internal data class ExerciseTransactionEntity(
    @PrimaryKey
    val id: String,
    val dateOfString: String,
    val dateOfEnd: String?,
    val isExecuted: Boolean,
    val isSynchronized: Boolean
)
