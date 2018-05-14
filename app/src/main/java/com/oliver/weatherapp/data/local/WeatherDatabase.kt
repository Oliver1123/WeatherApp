package com.oliver.weatherapp.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context

import com.oliver.weatherapp.data.local.dao.CitiesDao
import com.oliver.weatherapp.data.local.dao.WeatherDao
import com.oliver.weatherapp.data.local.model.CityEntry
import com.oliver.weatherapp.data.local.model.WeatherEntry
import timber.log.Timber

@Database(entities = [CityEntry::class, WeatherEntry::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun citiesDao(): CitiesDao

    abstract fun weatherDao(): WeatherDao

    companion object {
        private val DATABASE_NAME = "weather"

        // For Singleton instantiation
        private val LOCK = Any()
        @Volatile
        private var sInstance: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase {
            Timber.d("Getting the database")
            return sInstance ?: synchronized(LOCK) {
                sInstance ?: buildDatabase(context).also {
                    Timber.d("Made new database")
                    sInstance = it
                }
            }
        }

        private fun buildDatabase(context: Context): WeatherDatabase {
            return Room
                    .databaseBuilder(context.applicationContext, WeatherDatabase::class.java, DATABASE_NAME)
                    .build()
        }
    }
}

