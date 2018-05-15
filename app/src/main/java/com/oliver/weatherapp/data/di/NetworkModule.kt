package com.oliver.weatherapp.data.di

import com.oliver.weatherapp.AppExecutors
import com.oliver.weatherapp.data.remote.WeatherDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideWeatherDataSource(executors: AppExecutors): WeatherDataSource {
        return WeatherDataSource(executors)
    }
}