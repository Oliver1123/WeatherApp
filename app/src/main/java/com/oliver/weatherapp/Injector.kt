package com.oliver.weatherapp

import android.content.Context

import com.oliver.weatherapp.data.local.WeatherDatabase
import com.oliver.weatherapp.data.local.model.CityEntry
import com.oliver.weatherapp.data.remote.WeatherDataSource
import com.oliver.weatherapp.data.repositories.CitiesRepository
import com.oliver.weatherapp.data.repositories.WeatherRepository
import com.oliver.weatherapp.screens.forecast.WeatherViewModelFactory
import com.oliver.weatherapp.screens.home.CitiesViewModelFactory

object Injector {

    private fun provideCitiesRepository(context: Context): CitiesRepository {
        val database = WeatherDatabase.getInstance(context)
        val executors = AppExecutors.getInstance()
        return CitiesRepository.getInstance(executors, database.citiesDao())
    }

    private fun provideWeatherRepository(context: Context): WeatherRepository {
        val database = WeatherDatabase.getInstance(context)
        val executors = AppExecutors.getInstance()
        val dataSource = provideWeatherDataSource()
        return WeatherRepository.getInstance(executors, database.weatherDao(), dataSource)
    }

    private fun provideWeatherDataSource(): WeatherDataSource {
        val executors = AppExecutors.getInstance()
        return WeatherDataSource.getInstance(executors)
    }

    fun provideCitiesViewModelFactory(context: Context): CitiesViewModelFactory {
        val repository = provideCitiesRepository(context.applicationContext)
        return CitiesViewModelFactory(repository)
    }

    fun provideWeatherViewModelFactory(context: Context, city: CityEntry): WeatherViewModelFactory {
        val repository = provideWeatherRepository(context.applicationContext)
        return WeatherViewModelFactory(repository, city)
    }
}
