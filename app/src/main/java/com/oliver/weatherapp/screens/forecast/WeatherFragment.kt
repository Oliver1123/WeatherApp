package com.oliver.weatherapp.screens.forecast


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.oliver.weatherapp.R
import com.oliver.weatherapp.data.local.model.WeatherEntry
import com.oliver.weatherapp.screens.MainActivity
import com.oliver.weatherapp.screens.SelectedCitySharedViewModel
import com.oliver.weatherapp.screens.ViewModelFactory
import com.oliver.weatherapp.screens.base.BaseFragment
import com.oliver.weatherapp.utils.getAppComponent
import com.oliver.weatherapp.utils.getViewModel
import kotlinx.android.synthetic.main.fragment_weather.*
import timber.log.Timber
import javax.inject.Inject

class WeatherFragment : BaseFragment() {
    private val KEY_FIST_VISIBLE_POSITION = "KEY_FIST_VISIBLE_POSITION"
    private val DEFAULT_POSITION: Int = 0
    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: WeatherViewModel
    private lateinit var selectedCitySharedViewModel: SelectedCitySharedViewModel


    private lateinit var weatherAdapter: WeatherAdapter

    private var savedPositions = DEFAULT_POSITION


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAppComponent().inject(this)

        initViewModel()

        initUI(view)
        restoreState(savedInstanceState)
    }

    private fun initUI(view: View) {
        swipeContainer.setOnRefreshListener { this.refreshWeather() }
        initRecyclerView(view.context)
    }

    private fun setToolbarTitle(title: String) {
        val supportActionBar = (activity as AppCompatActivity).supportActionBar

        supportActionBar?.title = title

    }

    private fun initRecyclerView(context: Context) {
        weatherAdapter = WeatherAdapter(context)

        recycler_view_forecast.layoutManager = LinearLayoutManager(context)
        recycler_view_forecast.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recycler_view_forecast.itemAnimator = DefaultItemAnimator()

        recycler_view_forecast.adapter = weatherAdapter
    }

    private fun initViewModel() {
        viewModel = getViewModel(factory)

        viewModel.forecast.observe(this, Observer { onWeatherUpdated(it) })

        selectedCitySharedViewModel = ViewModelProviders.of(activity as MainActivity, factory)
                .get(SelectedCitySharedViewModel::class.java)
        selectedCitySharedViewModel.selectedCity.observe(this, Observer {
            it?.let {
                viewModel.setCity(it)
                setToolbarTitle(it.name ?: "")
            }
        })
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

        fun newInstance(): WeatherFragment {

            val args = Bundle()
            val fragment = WeatherFragment()
            fragment.arguments = args
            Timber.d( "newInstance: $fragment")
            return fragment
        }
    }
}
