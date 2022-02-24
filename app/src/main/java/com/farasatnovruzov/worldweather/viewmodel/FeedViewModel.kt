package com.farasatnovruzov.worldweather.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.farasatnovruzov.worldweather.model.Weather
import com.farasatnovruzov.worldweather.model.Weather_SQL
import com.farasatnovruzov.worldweather.repo.WeatherRepositoryRetrofit
import com.farasatnovruzov.worldweather.roomdb.Weather_SQLDao
import com.farasatnovruzov.worldweather.roomdb.Weather_SQLDatabase
import com.farasatnovruzov.worldweather.util.CustomSharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel
    @Inject constructor(
        application: Application,
        private val weatherAPIService: WeatherRepositoryRetrofit,

    ) : BaseViewModel(application) {

//      private val weatherAPIService = WeatherAPIService()
//      private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L
    private val customSharedPreferences = CustomSharedPreferences(getApplication())
    private val disposable = CompositeDisposable()
    private val disposable2 = CompositeDisposable()


//    val city = MutableLiveData<Weather>()
//    private val _citySql = MutableLiveData<Weather_SQLDao>()
//    val citySql : LiveData<Weather_SQLDao>
//        get() = _citySql

    val citySql = MutableLiveData<Weather_SQLDao>()
    val cityAPI = MutableLiveData<Weather>()

    val weatherLoading = MutableLiveData<Boolean>()
    val weatherError = MutableLiveData<Boolean>()


    /*
    fun refreshData() {

        val updateTime = customSharedPreferences.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < MySingleton.refreshTime) {
            getDataFromSQLite()
        } else {
            getDataFromAPIBaku()
        }
    }
     */


    fun getDataFromAPIBaku() {
        weatherLoading.value = true
        weatherError.value = false
        disposable.add(
            weatherAPIService
                .getBaku()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Weather>() {
                    override fun onSuccess(t: Weather) {
                        cityAPI.value = t

                        weatherError.value = false
                        weatherLoading.value = false
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




   fun getDataFromSQLite() {
       weatherLoading.value = true
       weatherError.value = false

       launch {
           val dao = Weather_SQLDatabase(getApplication()).weather_SQLDao()


           if (!dao.getWeatherItem().name.isNullOrEmpty()){
               weatherError.value = false
               weatherLoading.value = true
               citySql.value = dao
               weatherError.value = false
               weatherLoading.value = false
               Toast.makeText(getApplication(), "Weather from SQLite", Toast.LENGTH_SHORT).show()
           }else{
               weatherError.value = true
               weatherLoading.value = false
           }

       }
    }



   fun storeInSQLite(weatherSql: Weather_SQL) {
        launch {
            val dao = Weather_SQLDatabase(getApplication()).weather_SQLDao()
            dao.insertAll(weatherSql)  //*list.toTypedArray() list -> individual
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


