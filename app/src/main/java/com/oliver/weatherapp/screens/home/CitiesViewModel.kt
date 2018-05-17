package com.oliver.weatherapp.screens.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.oliver.weatherapp.domain.model.City
import com.oliver.weatherapp.domain.repositories.CitiesRepository
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class CitiesViewModel @Inject constructor(
        private val repository: CitiesRepository
) : ViewModel() {
    private val cities: MutableLiveData<List<City>> = MutableLiveData()
    private val disposable = CompositeDisposable()

    init {
        Timber.d("CitiesViewModel: constructor repository: $repository")
        disposable.add(repository.getCities()
                .subscribe(
                        cities::postValue,
                        { Timber.e(it) }
                )
        )
    }

    fun getCities(): LiveData<List<City>> = cities

    fun addCity(City: City) {
        repository.addCity(City)
    }

    fun deleteCity(city: City) {
        repository.deleteCity(city.id)
    }

    override fun onCleared() {
        super.onCleared()

        disposable.clear()
    }
}
