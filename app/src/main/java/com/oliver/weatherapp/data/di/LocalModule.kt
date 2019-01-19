package com.oliver.weatherapp.data.di

import android.arch.persistence.room.Room
import android.content.Context
import com.oliver.weatherapp.data.local.WeatherDatabase
import org.koin.dsl.module.module

val localModule = module {
    single { buildDatabase(get()) }
    single { (get() as WeatherDatabase).citiesDao() }
    single { (get() as WeatherDatabase).weatherDao() }
}

private fun buildDatabase(context: Context): WeatherDatabase {
    return Room.databaseBuilder(context, WeatherDatabase::class.java, "weather.db")
        .build()
}