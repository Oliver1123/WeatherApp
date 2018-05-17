package com.oliver.weatherapp.screens.forecast

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.oliver.weatherapp.data.local.model.WeatherEntry
import com.oliver.weatherapp.data.repositories.WeatherRepository
import com.oliver.weatherapp.domain.model.City
import javax.inject.Inject


class WeatherViewModel @Inject constructor(
        private val repository: WeatherRepository
) : ViewModel() {

    private val cityInput : MutableLiveData<City> = MutableLiveData()
    val forecast: LiveData<List<WeatherEntry>> = Transformations.switchMap(cityInput)  {
        city -> repository.getForecast(city.id, city.latitude, city.longitude)
    }

    fun setCity(city: City) {
        cityInput.value = city
    }

    fun refreshWeather() {
        cityInput.value?.apply {
            repository.forceWeatherRefresh(id, latitude, longitude)
        }
    }

}
