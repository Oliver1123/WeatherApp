package com.oliver.weatherapp.screens.forecast

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.oliver.weatherapp.domain.model.City
import com.oliver.weatherapp.domain.model.WeatherItem
import com.oliver.weatherapp.domain.repositories.WeatherRepository
import com.oliver.weatherapp.screens.Data
import com.oliver.weatherapp.screens.DataState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val data: MutableLiveData<Data<List<WeatherItem>>> = MutableLiveData()
    private var currentCity: City? = null
    private val disposable = CompositeDisposable()

    fun getData(): LiveData<Data<List<WeatherItem>>> = data

    fun setCity(city: City) {
        if (city != currentCity) {
            currentCity = city

            terminateLoading()
            disposable += repository.getForecast(city.id, city.latitude, city.longitude)
                .doOnSubscribe { data.postValue(Data(DataState.LOADING)) }
                .subscribeBy(
                    onNext = {
                        data.postValue(Data(DataState.SUCCESS, data = it))
                    },
                    onError = {
                        Timber.e(it)
                        data.postValue(Data(DataState.ERROR, message = it.message))
                    }
                )
        }
    }

    fun refreshWeather() {
        data.postValue(Data(DataState.LOADING))
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
