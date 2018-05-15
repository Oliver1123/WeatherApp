package com.oliver.weatherapp.screens

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.oliver.weatherapp.data.local.model.CityEntry
import javax.inject.Inject

class SelectedCitySharedViewModel @Inject constructor() : ViewModel() {
    private val selectedCity: MutableLiveData<CityEntry> = MutableLiveData()

    fun setCity(cityEntry: CityEntry) {
        selectedCity.postValue(cityEntry)
    }

    fun getSelectedCity(): LiveData<CityEntry> {
        return selectedCity
    }
}