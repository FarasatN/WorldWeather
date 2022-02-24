package com.farasatnovruzov.worldweather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.farasatnovruzov.worldweather.R
import com.farasatnovruzov.worldweather.databinding.ItemRowSqlBinding
import com.farasatnovruzov.worldweather.roomdb.Weather_SQLDatabase
import com.farasatnovruzov.worldweather.singleton.MySingleton
import com.farasatnovruzov.worldweather.util.CustomSharedPreferences
import com.farasatnovruzov.worldweather.view.FeedFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class WeatherAdapterSql() : RecyclerView.Adapter<WeatherAdapterSql.WeatherSqlViewHolder>(),
    CoroutineScope, WeatherSqlClickListener {

    class WeatherSqlViewHolder(var view: ItemRowSqlBinding) : RecyclerView.ViewHolder(view.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherSqlViewHolder {

        val inflater = LayoutInflater.from(parent.context)
//        val view = inflater.inflate(R.layout.item_row_sql, parent, false)
        val view = DataBindingUtil.inflate<ItemRowSqlBinding>(inflater,R.layout.item_row_sql,parent,false)
        return WeatherSqlViewHolder(view)

    }

    override fun onBindViewHolder(holder: WeatherSqlViewHolder, position: Int) {
        val customSharedPreferences =
            CustomSharedPreferences(holder.view.root.context.applicationContext)
        val refreshTime = 10 * 60 * 1000 * 1000 * 1000L
        val updateTime = customSharedPreferences.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            launch {
                val dao =
                    Weather_SQLDatabase(holder.view.root.context.applicationContext).weather_SQLDao()

                holder.view.weatherSql = dao.getWeatherItem()
                holder.view.listenerSql = this@WeatherAdapterSql
            }
        }

        /*
        launch {
            val dao =
                Weather_SQLDatabase(holder.view.context.applicationContext).weather_SQLDao()

            holder.view.city.text = dao.getWeatherItem().name
            holder.view.temperature.text = dao.getWeatherItem().temp_c
            holder.view.localtime.text = dao.getWeatherItem().localtime
            holder.view.imageView.getImage(
                dao.getWeatherItem().icon,
                placeholderProgressBar(holder.view.context)
            )
        }

        holder.view.setOnClickListener {
            val action = FeedFragmentDirections.actionFeedFragmentToDetailsFragment()
            Navigation.findNavController(it).navigate(action)
            MySingleton.recycle = true
        }

         */
    }

    override fun getItemCount(): Int {
        return 1
    }


    override fun onWeatherSqlClicked(v: View) {
        val action = FeedFragmentDirections.actionFeedFragmentToDetailsFragment()
        Navigation.findNavController(v).navigate(action)
        MySingleton.recycle = true
    }


    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

}