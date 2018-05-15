package com.oliver.weatherapp.data.di

import android.arch.persistence.room.Room
import android.content.Context
import com.oliver.weatherapp.data.local.WeatherDatabase
import com.oliver.weatherapp.data.local.dao.CitiesDao
import com.oliver.weatherapp.data.local.dao.WeatherDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): WeatherDatabase {
        return Room.databaseBuilder(context, WeatherDatabase::class.java, "weather.db")
                .build()
    }

    @Provides
    @Singleton
    fun provideCitiesDao(database: WeatherDatabase): CitiesDao {
        return database.citiesDao()
    }

    @Provides
    @Singleton
    fun provideWeatherDao(database: WeatherDatabase): WeatherDao {
        return database.weatherDao()
    }


}
