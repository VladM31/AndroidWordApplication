package can.lucky.of.exercise.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import can.lucky.of.exercise.db.entities.ExerciseEntity

@Dao
internal interface ExerciseDao {

    @Query("SELECT * FROM exercises WHERE transactionId = :id order by position asc")
    suspend fun findById(id: String) : List<ExerciseEntity>


    @Insert
    suspend fun insertAll(exercises: List<ExerciseEntity>)

    @Update
    suspend fun update(exercise: ExerciseEntity)
}