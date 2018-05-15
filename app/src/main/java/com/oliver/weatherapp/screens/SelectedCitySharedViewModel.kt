package com.oliver.weatherapp.screens

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.oliver.weatherapp.data.local.model.CityEntry
import javax.inject.Inject

class SelectedCitySharedViewModel @Inject constructor() : ViewModel() {
    val selectedCity: MutableLiveData<CityEntry> = MutableLiveData()
}