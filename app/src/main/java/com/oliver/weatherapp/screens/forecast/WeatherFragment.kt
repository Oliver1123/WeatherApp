package com.oliver.weatherapp.screens.forecast


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
import com.oliver.weatherapp.domain.model.WeatherItem
import com.oliver.weatherapp.screens.Data
import com.oliver.weatherapp.screens.DataState
import com.oliver.weatherapp.screens.MainActivity
import com.oliver.weatherapp.screens.base.BaseFragment
import com.oliver.weatherapp.utils.observe
import kotlinx.android.synthetic.main.fragment_weather.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class WeatherFragment : BaseFragment() {
    private val KEY_FIST_VISIBLE_POSITION = "KEY_FIST_VISIBLE_POSITION"
    private val DEFAULT_POSITION: Int = 0
    private val weatherViewModel: WeatherViewModel by viewModel()

    private lateinit var weatherAdapter: WeatherAdapter

    private var savedPositions = DEFAULT_POSITION


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        recycler_view_forecast.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        recycler_view_forecast.itemAnimator = DefaultItemAnimator()

        recycler_view_forecast.adapter = weatherAdapter
    }

    private fun initViewModel() {
        observe(weatherViewModel.getData()) {
            onDataUpdated(it)
        }

        observe((activity as MainActivity).sharedViewModel.getSelectedCity()) {
            it?.let { city ->
                weatherViewModel.setCity(city)
                setToolbarTitle(city.name ?: "")
            }
        }
    }

    private fun onDataUpdated(dataState: Data<List<WeatherItem>>?) {
        Timber.d("onDataUpdated: $dataState")

        dataState?.run {
            when (state) {
                DataState.LOADING -> {
                    pb_loader.visibility = View.VISIBLE
                }
                DataState.SUCCESS -> {
                    hideLoaders()
                    tv_error_message.visibility = View.INVISIBLE
                    recycler_view_forecast.visibility = View.VISIBLE
                }
                DataState.ERROR -> {
                    hideLoaders()

                    tv_error_message.visibility = View.VISIBLE
                    recycler_view_forecast.visibility = View.INVISIBLE
                }
            }
            data?.let { displayWeather(it) }
            message?.let { tv_error_message.text = it }
        }
    }

    private fun hideLoaders() {
        pb_loader.visibility = View.INVISIBLE
        swipeContainer.isRefreshing = false
    }

    private fun restoreState(savedInstanceState: Bundle?) {

        savedPositions = savedInstanceState?.getInt(
            KEY_FIST_VISIBLE_POSITION,
            RecyclerView.NO_POSITION
        ) ?: RecyclerView.NO_POSITION
    }

    // save scrolled position here, because onSaveInstanceState can be triggered after onDestroyView (view can be null)
    override fun onPause() {
        super.onPause()
        savedPositions =
                (recycler_view_forecast.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(KEY_FIST_VISIBLE_POSITION, savedPositions)
    }

    private fun refreshWeather() {
        savedPositions = DEFAULT_POSITION
        weatherViewModel.refreshWeather()
    }

    private fun displayWeather(weatherItem: List<WeatherItem>) {
        weatherAdapter.setForecast(weatherItem)

        tryToRestoreScrolledPosition(weatherItem.size)
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
            Timber.d("newInstance: $fragment")
            return fragment
        }
    }
}
