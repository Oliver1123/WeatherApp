package com.oliver.weatherapp.screens.forecast

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.oliver.weatherapp.domain.model.City
import com.oliver.weatherapp.domain.model.WeatherItem
import com.oliver.weatherapp.domain.repositories.WeatherRepository
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject


class WeatherViewModel @Inject constructor(
        private val repository: WeatherRepository
) : ViewModel() {

    private val cityForecast : MutableLiveData<List<WeatherItem>> = MutableLiveData()
    private var currentCity: City? = null
    private val disposable = CompositeDisposable()


    fun getForecast(): LiveData<List<WeatherItem>> = cityForecast


    fun setCity(city: City) {
        currentCity = city
        terminateLoading()
        disposable.add(
                repository.getForecast(city.id, city.latitude, city.longitude)
                        .subscribe(
                                cityForecast::postValue,
                                { Timber.e(it) }
                        )
        )
    }

    fun refreshWeather() {
        currentCity?.apply {
            repository.forceWeatherRefresh(id, latitude, longitude)
        }
    }

    override fun onCleared() {
        super.onCleared()

        terminateLoading()
    }

    private fun terminateLoading() {
        disposable.clear()
    }
}
