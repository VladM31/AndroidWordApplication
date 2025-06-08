package can.lucky.of.exercise.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import can.lucky.of.exercise.db.entities.ExerciseWordEntity

@Dao
internal interface ExerciseWordDao {

    @Query("SELECT * FROM exercise_words WHERE transactionId = :transactionId")
    suspend fun findByTransactionId(transactionId: String) : List<ExerciseWordEntity>

    @Query("SELECT * FROM exercise_words WHERE transactionId = :transactionId AND userWordId = :userWordId")
    suspend fun findByTransactionIdAndWordId(transactionId: String, userWordId: String) : ExerciseWordEntity?

    @Insert
    suspend fun insertAll(exerciseWords: List<ExerciseWordEntity>)

    @Update
    suspend fun update(exerciseWord: ExerciseWordEntity)

    @Update
    suspend fun updateAll(exerciseWords: List<ExerciseWordEntity>)
}