package com.oliver.weatherapp.screens.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.oliver.weatherapp.data.local.model.CityEntry
import com.oliver.weatherapp.data.repositories.CitiesRepository
import timber.log.Timber
import javax.inject.Inject

class CitiesViewModel @Inject constructor(
        private val repository: CitiesRepository
) : ViewModel() {
    val cities: LiveData<List<CityEntry>>

    init {
        Timber.d("CitiesViewModel: constructor repository: $repository")
        cities = repository.getCities()
    }

    fun addCity(cityEntry: CityEntry) {
        repository.addCity(cityEntry)
    }

    fun deleteCity(city: CityEntry) {
        repository.deleteCity(city)
    }
}
