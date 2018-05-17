package com.oliver.weatherapp.data.di

import com.oliver.weatherapp.AppExecutors
import com.oliver.weatherapp.data.local.dao.CitiesDao
import com.oliver.weatherapp.data.local.dao.WeatherDao
import com.oliver.weatherapp.data.remote.WeatherDataSource
import com.oliver.weatherapp.data.repositories.CitiesRepositoryImpl
import com.oliver.weatherapp.data.repositories.WeatherRepository
import com.oliver.weatherapp.domain.repositories.CitiesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideCitiesRepository(executors: AppExecutors, citiesDao: CitiesDao): CitiesRepository {
        return CitiesRepositoryImpl(executors, citiesDao)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(executors: AppExecutors, weatherDao: WeatherDao, dataSource: WeatherDataSource): WeatherRepository {
        return WeatherRepository(executors, weatherDao, dataSource)
    }
}