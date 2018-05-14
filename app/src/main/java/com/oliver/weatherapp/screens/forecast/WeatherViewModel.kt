package com.oliver.weatherapp.screens.forecast

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

import com.oliver.weatherapp.data.local.model.CityEntry
import com.oliver.weatherapp.data.local.model.WeatherEntry
import com.oliver.weatherapp.data.repositories.WeatherRepository
import timber.log.Timber

class WeatherViewModel(private val repository: WeatherRepository, private val city: CityEntry) : ViewModel() {
    val forecast: LiveData<List<WeatherEntry>>

    init {
        Timber.d( "WeatherViewModel: constructor repository: $repository")
        forecast = repository.getForecast(city.id, city.latitude, city.longitude)
    }

    fun refreshWeather() {
        repository.forceWeatherRefresh(city.id, city.latitude, city.longitude)
    }

}
