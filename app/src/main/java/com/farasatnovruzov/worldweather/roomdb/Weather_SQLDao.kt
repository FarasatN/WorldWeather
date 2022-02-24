package com.farasatnovruzov.worldweather.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.farasatnovruzov.worldweather.model.Weather_SQL


@Dao
interface Weather_SQLDao {

    //Data Acces Object

    //Insert -> INSERT INTO
    // suspend -> coroutine, pause & resume
    //vararg -> multiple weather objects
    //List<Long> -> primary keys

    @Insert
    suspend fun insertAll(vararg weather: Weather_SQL): List<Long>

    @Update
    suspend fun updateAll(weatherSql: Weather_SQL)

    @Query("SELECT * FROM Weather_SQL")
    suspend fun getWeatherItem(): Weather_SQL


    @Query("DELETE FROM Weather_SQL")
    suspend fun deleteWeather_SQL()

}