package com.farasatnovruzov.worldweather.service

import com.farasatnovruzov.worldweather.model.Weather
import com.farasatnovruzov.worldweather.singleton.MySingleton
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class WeatherAPIService {

    //    https://api.weatherapi.com/v1/forecast.json?key=ee155d6309a84ba8b7e75115210709&q=Xacmaz
    //BASE_URL -> https://api.weatherapi.com/v1/
    //EXT -> forecast.json?key=ee155d6309a84ba8b7e75115210709&q=Baku


    private val api = Retrofit.Builder()
        .baseUrl(MySingleton.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(WeatherAPI::class.java)

    fun getBaku(): Single<Weather> {
        return api.getBaku()
    }

    fun getCity(url: String): Single<Weather> {
        return api.getCity(url)
    }




    /*
    //Or with coroutine
    fun getBakuCoroutine(): Weather {
        return api
    }

    //Or with coroutine
    fun getCityCoroutine(url: String): Weather {
        return api.getCityCoroutine(url)
    }


     */

}