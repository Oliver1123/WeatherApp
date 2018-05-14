package com.oliver.weatherapp.screens.forecast

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

import com.oliver.weatherapp.data.local.model.CityEntry
import com.oliver.weatherapp.data.repositories.WeatherRepository

class WeatherViewModelFactory(
        private val mRepository: WeatherRepository,
        private val city: CityEntry
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return WeatherViewModel(mRepository, city) as T
    }
}