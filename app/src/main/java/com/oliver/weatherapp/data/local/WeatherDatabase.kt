package com.oliver.weatherapp.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.oliver.weatherapp.data.local.dao.CitiesDao
import com.oliver.weatherapp.data.local.dao.WeatherDao
import com.oliver.weatherapp.data.local.model.CityEntry
import com.oliver.weatherapp.data.local.model.WeatherEntry

@Database(
    entities = [
        CityEntry::class, WeatherEntry::class
    ],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun citiesDao(): CitiesDao

    abstract fun weatherDao(): WeatherDao
}

