package com.oliver.weatherapp.screens

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.oliver.weatherapp.domain.model.City

class SelectedCitySharedViewModel : ViewModel() {
    private val selectedCity: MutableLiveData<City> = MutableLiveData()

    fun setCity(city: City) {
        selectedCity.postValue(city)
    }

    fun getSelectedCity(): LiveData<City> {
        return selectedCity
    }
}