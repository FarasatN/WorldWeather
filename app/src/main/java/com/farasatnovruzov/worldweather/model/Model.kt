package com.farasatnovruzov.worldweather.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Weather_SQL(
    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "country")
    val country: String,

    @ColumnInfo(name = "temp_c")
    val temp_c: String,

    @ColumnInfo(name = "text")
    val text: String,

    @ColumnInfo(name = "localtime")
    val localtime: String,

    @ColumnInfo(name = "sun")
    val sun: String,

    @ColumnInfo(name = "icon")
    val icon: String,
) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0

}

data class Weather(
    @SerializedName("location") val location: Location,

    @SerializedName("current") val current: Current,

    @SerializedName("forecast") val forecast: Forecast

)


data class Location(

    @SerializedName("name") val name: String,
    @SerializedName("region") val region: String,
    @SerializedName("country") val country: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("tz_id") val tz_id: String,
    @SerializedName("localtime_epoch") val localtime_epoch: Int,
    @SerializedName("localtime") val localtime: String
)


data class Current(

    @SerializedName("last_updated") val last_updated: String,
    @SerializedName("temp_c") val temp_c: Double,
    @SerializedName("is_day") val is_day: Int,
    @SerializedName("condition") val condition: Condition,
    @SerializedName("uv") val uv: Int
)

data class Condition(
    @SerializedName("text") val text: String,
    @SerializedName("icon") val icon: String,
    @SerializedName("code") val code: Int
)

data class Forecast(

    @SerializedName("forecastday") val forecastday: List<Forecastday>
)

data class Forecastday(

    @SerializedName("date") val date: String,
    @SerializedName("day") val day: Day,
    @SerializedName("astro") val astro: Astro,
    @SerializedName("hour") val hour: List<Hour>
)

data class Astro(

    @SerializedName("sunrise") val sunrise: String,
    @SerializedName("sunset") val sunset: String
)

data class Day(

    @SerializedName("maxtemp_c") val maxtemp_c: Double,
    @SerializedName("mintemp_c") val mintemp_c: Double,
    @SerializedName("avgtemp_c") val avgtemp_c: Double,
    @SerializedName("maxwind_mph") val maxwind_mph: Double,
    @SerializedName("maxwind_kph") val maxwind_kph: Double,
    @SerializedName("totalprecip_mm") val totalprecip_mm: Double,
    @SerializedName("totalprecip_in") val totalprecip_in: Double,
    @SerializedName("daily_will_it_rain") val daily_will_it_rain: Int,
    @SerializedName("daily_chance_of_rain") val daily_chance_of_rain: Int,
    @SerializedName("daily_will_it_snow") val daily_will_it_snow: Int,
    @SerializedName("daily_chance_of_snow") val daily_chance_of_snow: Int,
    @SerializedName("condition") val condition: Condition,
    @SerializedName("uv") val uv: Int
)

data class Hour(

    @SerializedName("time") val time: String,
    @SerializedName("temp_c") val temp_c: Double,
    @SerializedName("is_day") val is_day: Int,
    @SerializedName("condition") val condition: Condition,
    @SerializedName("wind_mph") val wind_mph: Double,
    @SerializedName("wind_kph") val wind_kph: Double,
    @SerializedName("wind_degree") val wind_degree: Double
)





