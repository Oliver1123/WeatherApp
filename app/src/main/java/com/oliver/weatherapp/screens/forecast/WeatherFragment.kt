package com.oliver.weatherapp.screens.forecast


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.oliver.weatherapp.Injector
import com.oliver.weatherapp.R
import com.oliver.weatherapp.data.local.model.CityEntry
import com.oliver.weatherapp.data.local.model.WeatherEntry
import kotlinx.android.synthetic.main.fragment_weather.*
import timber.log.Timber

class WeatherFragment : Fragment() {
    private val KEY_FIST_VISIBLE_POSITION = "KEY_FIST_VISIBLE_POSITION"
    private val DEFAULT_POSITION: Int = 0

    private lateinit var viewModel: WeatherViewModel
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var city: CityEntry

    private var savedPositions = DEFAULT_POSITION


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        city = arguments?.getParcelable(ARG_CITY) ?: throw IllegalArgumentException("City should be passed in arguments")
        initViewModel()

        initUI(view)
        restoreState(savedInstanceState)
    }

    private fun initUI(view: View) {
        initToolbar()
        swipeContainer.setOnRefreshListener { this.refreshWeather() }
        initRecyclerView(view.context)
    }

    private fun initToolbar() {
        val supportActionBar = (activity as AppCompatActivity).supportActionBar

        supportActionBar?.title = city.name

    }

    private fun initRecyclerView(context: Context) {
        weatherAdapter = WeatherAdapter(context)

        recycler_view_forecast.layoutManager = LinearLayoutManager(context)
        recycler_view_forecast.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recycler_view_forecast.itemAnimator = DefaultItemAnimator()

        recycler_view_forecast.adapter = weatherAdapter
    }

    private fun initViewModel() {
        // Get the ViewModel from the factory
        val factory = Injector.provideWeatherViewModelFactory(context!!.applicationContext, city)
        viewModel = ViewModelProviders.of(this, factory).get(WeatherViewModel::class.java)
        viewModel.forecast.observe(this, Observer { this.onWeatherUpdated(it) })
    }



    private fun restoreState(savedInstanceState: Bundle?) {

        savedPositions = savedInstanceState?.getInt(KEY_FIST_VISIBLE_POSITION, RecyclerView.NO_POSITION) ?: RecyclerView.NO_POSITION
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val firstVisiblePosition = (recycler_view_forecast.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        outState.putInt(KEY_FIST_VISIBLE_POSITION, firstVisiblePosition)
    }

    private fun refreshWeather() {
        savedPositions = DEFAULT_POSITION
        viewModel.refreshWeather()
    }

    private fun onWeatherUpdated(forecast: List<WeatherEntry>?) {
        swipeContainer.isRefreshing = false
        if (forecast == null || forecast.isEmpty()) {
            showEmptyListResult()
        } else {
            displayWeather(forecast)
        }
    }

    private fun showEmptyListResult() {
        tv_empty_list_message.visibility = View.VISIBLE
        recycler_view_forecast.visibility = View.INVISIBLE
    }

    private fun displayWeather(weather: List<WeatherEntry>) {
        tv_empty_list_message.visibility = View.INVISIBLE
        recycler_view_forecast.visibility = View.VISIBLE

        weatherAdapter.setForecast(weather)

        tryToRestoreScrolledPosition(weather.size)
    }


    private fun tryToRestoreScrolledPosition(itemsCount: Int) {
        if (savedPositions != RecyclerView.NO_POSITION && savedPositions < itemsCount) {
            recycler_view_forecast.smoothScrollToPosition(savedPositions)
            savedPositions = RecyclerView.NO_POSITION
        }
    }

    companion object {
        private const val ARG_CITY = "ARG_CITY"

        fun newInstance(city: CityEntry): WeatherFragment {

            val args = Bundle()
            args.putParcelable(ARG_CITY, city)
            val fragment = WeatherFragment()
            fragment.arguments = args
            Timber.d( "newInstance: $fragment")
            return fragment
        }
    }
}
