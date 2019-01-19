package com.oliver.weatherapp.data.di

import com.oliver.weatherapp.data.repositories.CitiesRepositoryImpl
import com.oliver.weatherapp.data.repositories.WeatherRepositoryImpl
import com.oliver.weatherapp.domain.repositories.CitiesRepository
import com.oliver.weatherapp.domain.repositories.WeatherRepository
import org.koin.dsl.module.module

val repositoryModule = module {
    single<CitiesRepository> { CitiesRepositoryImpl(get(), get()) }
    single<WeatherRepository> { WeatherRepositoryImpl(get(), get(), get()) }
}