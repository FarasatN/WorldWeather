package com.farasatnovruzov.worldweather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.farasatnovruzov.worldweather.adapter.WeatherAdapterSql
import com.farasatnovruzov.worldweather.databinding.FragmentFeedBinding
import com.farasatnovruzov.worldweather.singleton.MySingleton
import com.farasatnovruzov.worldweather.util.CustomSharedPreferences
import com.farasatnovruzov.worldweather.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : Fragment(){

    private lateinit var viewModel: FeedViewModel
    private lateinit var viewModel2: FeedViewModel

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_feed, container, false)
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProvider(this).get(FeedViewModel::class.java)
        viewModel2 = ViewModelProvider(this).get(FeedViewModel::class.java)


        binding.getButton.setOnClickListener {

            MySingleton.search_city = binding.searchEditText.text.trim().toString()

            if (binding.searchEditText.text.trim().toString().isEmpty() || binding.searchEditText.text.trim()
                    .isBlank()
            ) {
                Toast.makeText(context, "Please enter a city", Toast.LENGTH_SHORT).show()

            } else {
                MySingleton.recycleSearch = true

                val action = FeedFragmentDirections.actionFeedFragmentToDetailsFragment()
                Navigation.findNavController(it).navigate(action)
                binding.searchEditText.setText("")

            }
        }

        val customSharedPreferences = CustomSharedPreferences(requireContext().applicationContext)
        val refreshTime = 10 * 60 * 1000 * 1000 * 1000L
        val updateTime = customSharedPreferences.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {


            binding.weatherList.layoutManager = LinearLayoutManager(context)
            val adapterSql = WeatherAdapterSql()
            binding.weatherList.adapter = adapterSql

            binding.errorTextView.visibility = View.GONE
            binding.weatherList.visibility = View.INVISIBLE
            binding.weatherLoading.visibility = View.VISIBLE
            try {
                viewModel2.getDataFromSQLite()
            }catch (e: Exception){
                e.printStackTrace()
                binding.weatherLoading.visibility = View.GONE
                binding.errorTextView.visibility = View.VISIBLE
            }

            binding.weatherList.visibility = View.VISIBLE
            binding.weatherLoading.visibility = View.GONE

            binding.swipeRefreshLayout.setOnRefreshListener {
                binding.weatherList.layoutManager = LinearLayoutManager(context)
                binding.weatherList.adapter = MySingleton.weatherAdapterAPI
                viewModel.getDataFromAPIBaku()
                observeLiveData()
                binding.swipeRefreshLayout.isRefreshing = false

            }

        }

        else{
            binding.weatherList.layoutManager = LinearLayoutManager(context)
            binding.weatherList.adapter = MySingleton.weatherAdapterAPI

            viewModel.getDataFromAPIBaku()
            observeLiveData()
            binding.swipeRefreshLayout.setOnRefreshListener {

                viewModel.getDataFromAPIBaku()
                observeLiveData()
                binding.swipeRefreshLayout.isRefreshing = false

            }

        }
    }


    private fun observeLiveData() {
        viewModel.cityAPI.observe(viewLifecycleOwner, Observer { cityAPI ->
            cityAPI?.let {
                binding.weatherList.visibility = View.VISIBLE
                MySingleton.weatherAdapterAPI.updateWeatherListAPI(cityAPI)
            }

        })


        viewModel.weatherLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    binding.weatherLoading.visibility = View.VISIBLE
                    binding.weatherList.visibility = View.INVISIBLE
                    binding.errorTextView.visibility = View.GONE
                } else {
                    binding.weatherLoading.visibility = View.GONE
                }
            }

        })

        viewModel.weatherError.observe(viewLifecycleOwner, Observer { error ->
            error.let {
                if (it) {
                    binding.errorTextView.visibility = View.VISIBLE
                    binding.weatherList.visibility = View.INVISIBLE
                    binding.weatherLoading.visibility = View.GONE

                }else {
                    binding.errorTextView.visibility = View.GONE
                }
            }

        })

    }


}