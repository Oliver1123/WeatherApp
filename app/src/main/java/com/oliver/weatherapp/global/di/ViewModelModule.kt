package com.oliver.weatherapp.global.di


import com.oliver.weatherapp.screens.SelectedCitySharedViewModel
import com.oliver.weatherapp.screens.forecast.WeatherViewModel
import com.oliver.weatherapp.screens.home.CitiesViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel { CitiesViewModel(get()) }
    viewModel { WeatherViewModel(get()) }
    viewModel { SelectedCitySharedViewModel() }
}