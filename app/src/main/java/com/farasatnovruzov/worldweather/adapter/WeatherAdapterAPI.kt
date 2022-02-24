package com.farasatnovruzov.worldweather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.farasatnovruzov.worldweather.databinding.ItemRowApiBinding
import com.farasatnovruzov.worldweather.model.Weather
import com.farasatnovruzov.worldweather.singleton.MySingleton
import com.farasatnovruzov.worldweather.util.getImage
import com.farasatnovruzov.worldweather.util.placeholderProgressBar
import com.farasatnovruzov.worldweather.view.FeedFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class WeatherAdapterAPI(val weatherList: ArrayList<Weather>) :
    RecyclerView.Adapter<WeatherAdapterAPI.WeatherApiViewHolder>(), CoroutineScope,
    WeatherSqlClickListener {
    inner class WeatherApiViewHolder(var binding: ItemRowApiBinding) :
        RecyclerView.ViewHolder(binding.root)

//    private val diffCallBack = object : DiffUtil.ItemCallback<Weather>(){
//        override fun areItemsTheSame(oldItem: Weather, newItem: Weather): Boolean {
//            return oldItem.location.name == newItem.location.name
//        }
//
//        override fun areContentsTheSame(oldItem: Weather, newItem: Weather): Boolean {
//            return oldItem == newItem
//        }
//
//    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherApiViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val view = inflater.inflate(R.layout.item_row, parent, false)
        val binding = ItemRowApiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherApiViewHolder(binding)


    }

    override fun onBindViewHolder(holder: WeatherApiViewHolder, position: Int) {

//        holder.view.weatherApi = weatherListAPI[position]
//        holder.view.listenerApi = this@WeatherAdapterAPI


            holder.binding.city.text = weatherList[position].location.name
            holder.binding.temperature.text =
                weatherList[position].current.temp_c.toString() + " °C"
            holder.binding.localtime.text = weatherList[position].location.localtime
            holder.binding.imageView.getImage(
                "https:" + weatherList[position].current.condition.icon,
                placeholderProgressBar(holder.binding.root.context)
            )
            holder.itemView.setOnClickListener {
                val action = FeedFragmentDirections.actionFeedFragmentToDetailsFragment()
                Navigation.findNavController(it).navigate(action)
                MySingleton.recycleAPI = true
            }

//        val picasso = Picasso.get()
//        picasso.load("https:"+weatherList[position].current.condition.icon).into(holder.view.imageView)

    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    fun updateWeatherListAPI(newWeatherListAPI: Weather) {
        weatherList.clear()
        weatherList.add(newWeatherListAPI)
        notifyDataSetChanged()
    }


    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onWeatherSqlClicked(v: View) {
        val action = FeedFragmentDirections.actionFeedFragmentToDetailsFragment()
        Navigation.findNavController(v).navigate(action)
        MySingleton.recycle = true
    }


}