package can.lucky.of.exercise.di

import androidx.room.Room
import can.lucky.of.exercise.db.WordRoomDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


internal val dataBaseModule = module {
    single { Room.databaseBuilder(androidContext(), WordRoomDataBase::class.java, "word_android").build() }
    single { get<WordRoomDataBase>().exerciseDao() }
    single { get<WordRoomDataBase>().exerciseWordDao() }
    single { get<WordRoomDataBase>().exerciseTransactionDao() }
}