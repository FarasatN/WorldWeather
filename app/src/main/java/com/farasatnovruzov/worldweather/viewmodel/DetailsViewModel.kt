package com.farasatnovruzov.worldweather.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.farasatnovruzov.worldweather.model.Weather
import com.farasatnovruzov.worldweather.model.Weather_SQL
import com.farasatnovruzov.worldweather.repo.WeatherRepositoryRetrofit
import com.farasatnovruzov.worldweather.roomdb.Weather_SQLDao
import com.farasatnovruzov.worldweather.roomdb.Weather_SQLDatabase
import com.farasatnovruzov.worldweather.singleton.MySingleton
import com.farasatnovruzov.worldweather.util.CustomSharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel
@Inject constructor (application: Application,
private val weatherAPIService: WeatherRepositoryRetrofit,
) : BaseViewModel(application) {


//      private val weatherAPIService = WeatherAPIService()
        private val customSharedPreferences = CustomSharedPreferences(getApplication())
        private val disposable = CompositeDisposable()
        private val disposable2 = CompositeDisposable()


        val cityBakuSql = MutableLiveData<Weather_SQLDao>()
        val cityBaku = MutableLiveData<Weather>()
        val searching_city = MutableLiveData<Weather>()
        val weatherError = MutableLiveData<Boolean>()
        val weatherLoading = MutableLiveData<Boolean>()



        fun getDataFromSQLiteDetails() {
            weatherLoading.value = true
            weatherError.value = false

            launch {
                val dao = Weather_SQLDatabase(getApplication()).weather_SQLDao()

                if (!dao.getWeatherItem().name.isNullOrEmpty()){
                    weatherError.value = false
                    weatherLoading.value = true
                    cityBakuSql.value = dao
                    weatherError.value = false
                    weatherLoading.value = false
                    Toast.makeText(getApplication(), "Weather from SQLite", Toast.LENGTH_SHORT).show()
                }else{
                    weatherError.value = true
                    weatherLoading.value = false
                }

            }
        }


        fun getDataFromAPIBaku() {
            weatherLoading.value = true
            weatherError.value = false

            disposable2.add(
                weatherAPIService
                    .getBaku()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Weather>() {
                        override fun onSuccess(t: Weather) {
                            weatherLoading.value = true
                            weatherError.value = false

                            cityBaku.value = t

                            val weatherSql = Weather_SQL(
                                t.location.name,
                                t.location.country,
                                (t.current.temp_c.toString() + " °C"),
                                t.current.condition.text,
                                t.location.localtime,
                                ("Sun Rises: " + t.forecast.forecastday.get(0).astro.sunrise + "\n" + "Sun Sets: " + t.forecast.forecastday.get(
                                    0
                                ).astro.sunset),
                                ("https:" + t.current.condition.icon)

                            )

                            clearSQLite()
                            storeInSQLite(weatherSql)


                            Toast.makeText(getApplication(), "Weather from API", Toast.LENGTH_SHORT)
                                .show()
                            weatherLoading.value = false
                            weatherError.value = false

                        }

                        override fun onError(e: Throwable) {
                            weatherLoading.value = false
                            weatherError.value = true
                            e.printStackTrace()
                        }
                    })

            )
        }


        fun getCityDetails() {
            weatherLoading.value = true
            weatherError.value = false

            disposable.add(
                weatherAPIService
                    .getCity("forecast.json?key=ee155d6309a84ba8b7e75115210709&q=${MySingleton.search_city}")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Weather>() {
                        override fun onSuccess(t: Weather) {
                            weatherLoading.value = true
                            weatherError.value = false

                            searching_city.value = t

                            weatherLoading.value = false
                            weatherError.value = false



                        }override fun onError(e: Throwable) {
                            weatherLoading.value = false
                            weatherError.value = true
                            e.printStackTrace()

                        }
                    })

            )
        }

        fun storeInSQLite(weatherSql: Weather_SQL) {
            launch {
                val dao = Weather_SQLDatabase(getApplication()).weather_SQLDao()
                dao.insertAll(weatherSql)
                customSharedPreferences.saveTime(System.nanoTime())

            }
        }

        /*
        fun updateInSQLite(weatherSql: Weather_SQL) {
            launch {
                val dao = Weather_SQLDatabase(getApplication()).weather_SQLDao()
                dao.updateAll(weatherSql)
                customSharedPreferences.saveTime(System.nanoTime())

            }
        }

         */

        fun clearSQLite() {
            launch {
                val dao = Weather_SQLDatabase(getApplication()).weather_SQLDao()
                dao.deleteWeather_SQL()
            }
        }


        override fun onCleared() {
            super.onCleared()
            disposable.clear()
            disposable2.clear()

        }

}

