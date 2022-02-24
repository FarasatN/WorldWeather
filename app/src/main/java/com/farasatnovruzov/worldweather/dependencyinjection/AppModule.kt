package com.farasatnovruzov.worldweather.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.farasatnovruzov.worldweather.roomdb.Weather_SQLDatabase
import com.farasatnovruzov.worldweather.service.WeatherAPI
import com.farasatnovruzov.worldweather.singleton.MySingleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideBaseUrl() = MySingleton.BASE_URL

    @Singleton
    @Provides
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context, Weather_SQLDatabase::class.java, "WeatherDB"
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: Weather_SQLDatabase) = database.weather_SQLDao()

    @Singleton
    @Provides
    fun provideRetrofitAPI(BASE_URL: String): WeatherAPI {

        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(WeatherAPI::class.java)

    }



}