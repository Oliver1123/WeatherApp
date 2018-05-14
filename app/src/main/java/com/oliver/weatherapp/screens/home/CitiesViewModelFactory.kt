package com.oliver.weatherapp.screens.home

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

import com.oliver.weatherapp.data.repositories.CitiesRepository

class CitiesViewModelFactory(private val repository: CitiesRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return CitiesViewModel(repository) as T
    }
}