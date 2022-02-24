package com.farasatnovruzov.worldweather.repo

import com.farasatnovruzov.worldweather.model.Weather_SQL
import com.farasatnovruzov.worldweather.roomdb.Weather_SQLDao
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WeatherRepositorySQL @Inject constructor(
    private val weather_SqlDao : Weather_SQLDao
) {
    suspend fun insertAll(vararg weather: Weather_SQL): List<Long> {
        return weather_SqlDao.insertAll(*weather)
    }

    suspend fun updateAll(weatherSql: Weather_SQL) {
        return weather_SqlDao.updateAll(weatherSql)
    }

    suspend fun getWeatherItem(): Weather_SQL {
        return weather_SqlDao.getWeatherItem()
    }

    suspend fun deleteWeather_SQL() {
        weather_SqlDao.deleteWeather_SQL()
    }
}