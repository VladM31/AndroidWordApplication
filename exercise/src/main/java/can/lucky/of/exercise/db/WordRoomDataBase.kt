package can.lucky.of.exercise.db

import androidx.room.Database
import androidx.room.RoomDatabase
import can.lucky.of.exercise.db.dao.ExerciseDao
import can.lucky.of.exercise.db.dao.ExerciseTransactionDao
import can.lucky.of.exercise.db.dao.ExerciseWordDao
import can.lucky.of.exercise.db.entities.*;

@Database(
    entities = [
        ExerciseEntity::class,
        ExerciseTransactionEntity::class,
        ExerciseWordEntity::class
    ],
    version = 1
)
internal abstract class WordRoomDataBase : RoomDatabase() {

    abstract fun exerciseTransactionDao(): ExerciseTransactionDao

    abstract fun exerciseDao(): ExerciseDao

    abstract fun exerciseWordDao(): ExerciseWordDao
}