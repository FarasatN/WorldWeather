package com.farasatnovruzov.worldweather.service

import com.farasatnovruzov.worldweather.model.Weather
import com.farasatnovruzov.worldweather.singleton.MySingleton
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url


interface WeatherAPI {

    //GET, POST
    //BASE_URL -> https://api.weatherapi.com/v1/
    //EXT -> forecast.json?key=ee155d6309a84ba8b7e75115210709&q=Baku

    @GET(MySingleton.ext)
    fun getBaku(): Single<Weather>


    @GET
    fun getCity(@Url url: String): Single<Weather>




    //with coroutine
//    @GET(MySingleton.ext)
//    suspend fun getBakuCoroutine(): Weather
//
//
//
//    @GET
//    suspend fun getCityCoroutine(@Url url: String): Weather

}