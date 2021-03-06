package com.example.quiz.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.quiz.model.converters.Converters
import com.example.quiz.model.dao.ResultDao
import com.example.quiz.model.entities.Result

@Database(
    entities = [Result::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ResultDatabase : RoomDatabase() {
    abstract fun resultDao(): ResultDao

    companion object {
        @Volatile
        private var INSTANCE: ResultDatabase? = null
        private const val NAME = "result_database"

        fun getDatabase(context: Context): ResultDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) return tempInstance
            else
                synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        ResultDatabase::class.java,
                        NAME
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                    return instance
                }
        }
    }
}