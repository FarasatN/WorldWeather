package com.farasatnovruzov.worldweather.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.farasatnovruzov.worldweather.model.Weather_SQL

@Database(entities = arrayOf(Weather_SQL::class), version = 4, exportSchema = false)
abstract class Weather_SQLDatabase : RoomDatabase() {

    abstract fun weather_SQLDao(): Weather_SQLDao

    //Singleton

    companion object {
        @Volatile
        private var instance: Weather_SQLDatabase? = null

        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }

        private fun makeDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, Weather_SQLDatabase::class.java, "weatherdatabase"
        ).build()
    }
}