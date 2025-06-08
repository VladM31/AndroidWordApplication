package can.lucky.of.exercise.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import can.lucky.of.exercise.db.entities.ExerciseTransactionEntity

@Dao
internal interface ExerciseTransactionDao {

    @Query("SELECT * FROM exercise_transactions WHERE id = :id")
    suspend fun findById(id: String) : ExerciseTransactionEntity?

    @Insert
    suspend fun insert(exerciseTransaction: ExerciseTransactionEntity)

    @Update
    suspend fun update(exerciseTransaction: ExerciseTransactionEntity)

    @Query("DELETE FROM exercise_transactions WHERE id = :id")
    suspend fun delete(id: String)

}