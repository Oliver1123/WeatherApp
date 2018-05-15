package com.oliver.weatherapp.global.di


import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.oliver.weatherapp.screens.SelectedCitySharedViewModel

import com.oliver.weatherapp.screens.ViewModelFactory
import com.oliver.weatherapp.screens.forecast.WeatherViewModel
import com.oliver.weatherapp.screens.home.CitiesViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CitiesViewModel::class)
    abstract fun bindCitiesViewModel(viewModel: CitiesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WeatherViewModel::class)
    abstract fun bindWeatherViewModel(viewModel: WeatherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectedCitySharedViewModel::class)
    abstract fun bindSelectedCitySharedViewModel(viewModel: SelectedCitySharedViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}