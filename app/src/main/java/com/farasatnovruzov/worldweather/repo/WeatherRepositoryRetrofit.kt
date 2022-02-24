package com.farasatnovruzov.worldweather.repo

import com.farasatnovruzov.worldweather.model.Weather
import com.farasatnovruzov.worldweather.service.WeatherAPI
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryRetrofit @Inject constructor(
    private val weatherApi: WeatherAPI
){
    fun getBaku(): Single<Weather> {
        return weatherApi.getBaku()
    }

    fun getCity(url: String): Single<Weather> {
        return weatherApi.getCity(url)
    }
}