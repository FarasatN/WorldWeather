package com.farasatnovruzov.worldweather.singleton

import com.farasatnovruzov.worldweather.adapter.WeatherAdapterAPI

class MySingleton {

    companion object {
        var recycle = false
        var recycleAPI = false
        var recycleSearch = false
        val weatherAdapterAPI = WeatherAdapterAPI(arrayListOf())
        var search_city = ""
        const val BASE_URL = "https://api.weatherapi.com/v1/"
        const val  ext = "forecast.json?key=ee155d6309a84ba8b7e75115210709&q=Baku"
        const val refreshTime = 10 * 60 * 1000 * 1000 * 1000L
    }


}