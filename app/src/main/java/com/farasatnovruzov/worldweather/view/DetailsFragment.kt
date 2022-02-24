package com.farasatnovruzov.worldweather.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.farasatnovruzov.worldweather.databinding.FragmentDetailsBinding
import com.farasatnovruzov.worldweather.singleton.MySingleton
import com.farasatnovruzov.worldweather.util.CustomSharedPreferences
import com.farasatnovruzov.worldweather.util.getImage
import com.farasatnovruzov.worldweather.util.placeholderProgressBar
import com.farasatnovruzov.worldweather.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var viewModel: DetailsViewModel
    private lateinit var viewModel2: DetailsViewModel

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

//    private var cityUuid = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_details,container,false)
//        return inflater.inflate(R.layout.fragment_details, container, false)
//        return dataBinding.root

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        viewModel2 = ViewModelProvider(this).get(DetailsViewModel::class.java)


        val customSharedPreferences = CustomSharedPreferences(requireActivity().applicationContext)
        val refreshTime = 10 * 60 * 1000 * 1000 * 1000L
        val updateTime = customSharedPreferences.getTime()



        if (MySingleton.recycle == true) {

//                weatherLoading2.visibility = View.VISIBLE
            if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {

                viewModel.getDataFromSQLiteDetails()
                observeLiveData2(view.context)

            }
            MySingleton.recycle = false


            binding.swipeRefreshLayout2.setOnRefreshListener {
                binding.errorText.visibility = View.GONE

                binding.cityName.visibility = View.GONE
                binding.cityCountry.visibility = View.GONE
                binding.cityTemperature.visibility = View.GONE
                binding.cityText.visibility = View.GONE
                binding.citySun.visibility = View.GONE
                binding.cityImage.visibility = View.GONE
                binding.weatherLoading2.visibility = View.GONE

                viewModel2.getDataFromAPIBaku()
                observeLiveData3(view.context.applicationContext)

                binding.swipeRefreshLayout2.isRefreshing = false

                binding.cityName.visibility = View.VISIBLE
                binding.cityCountry.visibility = View.VISIBLE
                binding.cityTemperature.visibility = View.VISIBLE
                binding.cityText.visibility = View.VISIBLE
                binding.citySun.visibility = View.VISIBLE
                binding.cityImage.visibility = View.VISIBLE

                MySingleton.recycle = false

            }
            MySingleton.recycle = false

        } else if (MySingleton.recycleAPI == true) {

            viewModel2.getDataFromAPIBaku()
            observeLiveData3(view.context.applicationContext)

            MySingleton.recycleAPI = false

            binding.swipeRefreshLayout2.setOnRefreshListener {
                binding.errorText.visibility = View.GONE

                binding.cityName.visibility = View.GONE
                binding.cityCountry.visibility = View.GONE
                binding.cityTemperature.visibility = View.GONE
                binding.cityText.visibility = View.GONE
                binding.citySun.visibility = View.GONE
                binding.cityImage.visibility = View.GONE
                binding.weatherLoading2.visibility = View.GONE

                viewModel2.getDataFromAPIBaku()
                observeLiveData3(view.context.applicationContext)

                binding.swipeRefreshLayout2.isRefreshing = false

                binding.cityName.visibility = View.VISIBLE
                binding.cityCountry.visibility = View.VISIBLE
                binding.cityTemperature.visibility = View.VISIBLE
                binding.cityText.visibility = View.VISIBLE
                binding.citySun.visibility = View.VISIBLE
                binding.cityImage.visibility = View.VISIBLE

                MySingleton.recycleAPI = false

            }

            MySingleton.recycleAPI = false

        } else if(MySingleton.recycleSearch == true) {
            binding.cityName.visibility = View.GONE
            binding.cityCountry.visibility = View.GONE
            binding.cityTemperature.visibility = View.GONE
            binding.cityText.visibility = View.GONE
            binding.citySun.visibility = View.GONE
            binding.cityImage.visibility = View.GONE

            viewModel2.getCityDetails()
            observeLiveData(view.context)
            binding.errorText.visibility = View.GONE

            binding.weatherLoading2.visibility = View.GONE

            binding.cityName.visibility = View.VISIBLE
            binding.cityCountry.visibility = View.VISIBLE
            binding.cityTemperature.visibility = View.VISIBLE
            binding.cityText.visibility = View.VISIBLE
            binding.citySun.visibility = View.VISIBLE
            binding.cityImage.visibility = View.VISIBLE

            MySingleton.recycleSearch = false

            binding.swipeRefreshLayout2.setOnRefreshListener {
                binding.cityName.visibility = View.GONE
                binding.cityCountry.visibility = View.GONE
                binding.cityTemperature.visibility = View.GONE
                binding.cityText.visibility = View.GONE
                binding.citySun.visibility = View.GONE
                binding.cityImage.visibility = View.GONE
                binding.errorText.visibility = View.GONE
                binding.weatherLoading2.visibility = View.GONE

                viewModel2.getCityDetails()
                observeLiveData(view.context.applicationContext)


                binding.swipeRefreshLayout2.isRefreshing = false

                binding.cityName.visibility = View.VISIBLE
                binding.cityCountry.visibility = View.VISIBLE
                binding.cityTemperature.visibility = View.VISIBLE
                binding.cityText.visibility = View.VISIBLE
                binding.citySun.visibility = View.VISIBLE
                binding.cityImage.visibility = View.VISIBLE

                MySingleton.recycleSearch = false
            }
            MySingleton.recycleSearch = false
        }


//        arguments?.let {
//            cityUuid = DetailsFragmentArgs.fromBundle(it).cityUuid
//
//            if (MySingleton.recycle == true) {
//                println(cityUuid)
//
//                viewModel.getDataFromSQLiteDetails()
//                observeLiveData2(view.context)
//            }
//        }
//
//        viewModel2.getCityDetails()
//        observeLiveData(view.context.applicationContext)

    }


    private fun observeLiveData(context: Context) {
        viewModel.searching_city.observe(viewLifecycleOwner, Observer { detail ->
            detail?.let {
//                dataBinding.selectedOrSerchedCity = detail
                binding.cityName.text = detail.location.name
                binding.cityCountry.text = detail.location.country
                binding.cityTemperature.text = (detail.current.temp_c.toString() + " °C")
                binding.cityText.text = detail.current.condition.text
                binding.cityTime.text = detail.location.localtime
                binding.citySun.text =
                    ("Sun Rises: " + detail.forecast.forecastday.get(0).astro.sunrise + "\n" + "Sun Sets: " + detail.forecast.forecastday.get(
                        0
                    ).astro.sunset)
                binding.cityImage.getImage(
                    "https:" + detail.current.condition.icon,
                    placeholderProgressBar(context)
                )

            }

        })

        viewModel.weatherLoading.observe(viewLifecycleOwner, Observer { weatherLoading ->
            weatherLoading?.let {
                if (it) {
                    binding.weatherLoading2.visibility = View.VISIBLE
                    binding.errorText.visibility = View.GONE


                } else {
                    binding.weatherLoading2.visibility = View.GONE
                }
            }
        })

        viewModel.weatherError.observe(viewLifecycleOwner, Observer { error ->
            error.let {
                if (it) {
                    binding.errorText.visibility = View.VISIBLE
                    binding.weatherLoading2.visibility = View.GONE

                    binding.cityName.visibility = View.INVISIBLE
                    binding.cityCountry.visibility = View.INVISIBLE
                    binding.cityTemperature.visibility = View.INVISIBLE
                    binding.cityText.visibility = View.INVISIBLE
                    binding.cityTime.visibility = View.INVISIBLE
                    binding.citySun.visibility = View.INVISIBLE
                    binding.cityImage.visibility = View.INVISIBLE
                    binding.errorText.visibility = View.VISIBLE
                } else {
                    binding.errorText.visibility = View.GONE
                }
            }

        })
    }

    private fun observeLiveData2(context: Context) {
        viewModel.cityBakuSql.observe(viewLifecycleOwner, Observer { city ->
            city?.let {
                viewModel.launch {

//                    dataBinding.selectedOrSerchedCity = detail
                    binding.cityName.text = city.getWeatherItem().name
                    binding.cityCountry.text = city.getWeatherItem().country
                    binding.cityTemperature.text = city.getWeatherItem().temp_c
                    binding.cityText.text = city.getWeatherItem().text
                    binding.cityTime.text = city.getWeatherItem().localtime
                    binding.citySun.text = city.getWeatherItem().sun
                    binding.cityImage.getImage(
                        city.getWeatherItem().icon,
                        placeholderProgressBar(context)
                    )
                }

            }

        })

        viewModel.weatherLoading.observe(viewLifecycleOwner, Observer { weatherLoading ->
            weatherLoading?.let {
                if (it) {

                    binding.errorText.visibility = View.GONE
                    binding.weatherLoading2.visibility = View.VISIBLE

                } else {

                    binding.weatherLoading2.visibility = View.GONE
                }
            }
        })

        viewModel.weatherError.observe(viewLifecycleOwner, Observer { error ->
            error.let {
                if (it) {
                    binding.errorText.visibility = View.VISIBLE
                    binding.weatherLoading2.visibility = View.GONE

                    binding.cityName.visibility = View.INVISIBLE
                    binding.cityCountry.visibility = View.INVISIBLE
                    binding.cityTemperature.visibility = View.INVISIBLE
                    binding.cityText.visibility = View.INVISIBLE
                    binding.cityTime.visibility = View.INVISIBLE
                    binding.citySun.visibility = View.INVISIBLE
                    binding.cityImage.visibility = View.INVISIBLE
                    binding.errorText.visibility = View.VISIBLE

                } else {
                    binding.errorText.visibility = View.GONE
                }
            }

        })
    }


    private fun observeLiveData3(context: Context) {
        viewModel.cityBaku.observe(viewLifecycleOwner, Observer { cityBaku ->
            cityBaku?.let {
//            dataBinding.selectedOrSerchedCity = detail
                binding.cityName.text = cityBaku.location.name
                binding.cityCountry.text = cityBaku.location.country
                binding.cityTemperature.text = (cityBaku.current.temp_c.toString() + " °C")
                binding.cityText.text = cityBaku.current.condition.text
                binding.cityTime.text = cityBaku.location.localtime
                binding.citySun.text =
                    ("Sun Rises: " + cityBaku.forecast.forecastday.get(0).astro.sunrise + "\n" + "Sun Sets: " + cityBaku.forecast.forecastday.get(
                        0
                    ).astro.sunset)
                binding.cityImage.getImage(
                    "https:" + cityBaku.current.condition.icon,
                    placeholderProgressBar(context)
                )

            }

        })

        viewModel.weatherLoading.observe(viewLifecycleOwner, Observer { weatherLoading ->
            weatherLoading?.let {
                if (it) {
                    binding.weatherLoading2.visibility = View.VISIBLE
                    binding.errorText.visibility = View.GONE


                } else {
                    binding.weatherLoading2.visibility = View.GONE
                }
            }
        })

        viewModel.weatherError.observe(viewLifecycleOwner, Observer { error ->
            error.let {
                if (it) {
                    binding.errorText.visibility = View.VISIBLE
                    binding.weatherLoading2.visibility = View.GONE

                    binding.cityName.visibility = View.INVISIBLE
                    binding.cityCountry.visibility = View.INVISIBLE
                    binding.cityTemperature.visibility = View.INVISIBLE
                    binding.cityText.visibility = View.INVISIBLE
                    binding.cityTime.visibility = View.INVISIBLE
                    binding.citySun.visibility = View.INVISIBLE
                    binding.cityImage.visibility = View.INVISIBLE
                    binding.errorText.visibility = View.VISIBLE

                } else {
                    binding.errorText.visibility = View.GONE
                }
            }

        })
    }


}


//                }else{
//                        arguments?.let {
//                            cityUuid = DetailsFragmentArgs.fromBundle(it).cityUuid
//                            viewModel2.getDataFromAPIBaku()
////                            errorText.visibility = View.GONE
//                            cityName.text = weatherAdapter.weatherList[cityUuid].location.name
//                            cityCountry.text = weatherAdapter.weatherList[cityUuid].location.country
//                            cityTemperature.text =
//                                weatherAdapter.weatherList[cityUuid].current.temp_c.toInt()
//                                    .toString() + " °C"
//                            cityText.text =
//                                weatherAdapter.weatherList[cityUuid].current.condition.text
//                            cityTime.text = weatherAdapter.weatherList[cityUuid].location.localtime
//                            citySun.text =
//                                "Sun Rises: " + weatherAdapter.weatherList[cityUuid].forecast.forecastday.get(
//                                    0
//                                ).astro.sunrise + "\n" + "Sun Sets: " + weatherAdapter.weatherList[cityUuid].forecast.forecastday.get(
//                                    0
//                                ).astro.sunset
//                            cityImage.getImage(
//                                "https:" + weatherAdapter.weatherList[cityUuid].current.condition.icon,
//                                placeholderProgressBar(view.context)
//                            )
//                        }