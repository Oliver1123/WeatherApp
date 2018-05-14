package com.oliver.weatherapp.data.repositories

import android.arch.lifecycle.LiveData
import com.oliver.weatherapp.AppExecutors
import com.oliver.weatherapp.data.local.dao.CitiesDao
import com.oliver.weatherapp.data.local.model.CityEntry
import timber.log.Timber

class CitiesRepository private constructor(
        private val executors: AppExecutors,
        private val citiesDao: CitiesDao) {

    fun getCities(): LiveData<List<CityEntry>> = citiesDao.getAll()

    fun addCity(cityEntry: CityEntry) {
        Timber.d("addCity: $cityEntry")
        executors.diskIO().execute { citiesDao.insert(cityEntry) }
    }

    fun deleteCity(city: CityEntry) {
        executors.diskIO().execute { citiesDao.deleteCity(city.id) }
    }

    companion object {
        // For Singleton instantiation
        private val LOCK = Any()
        @Volatile
        private var sInstance: CitiesRepository? = null

        fun getInstance(executors: AppExecutors, dao: CitiesDao): CitiesRepository {
            Timber.d("Get the CitiesRepository")
            return sInstance ?: synchronized(LOCK) {
                sInstance ?: CitiesRepository(executors, dao).also {
                    Timber.d("Made new CitiesRepository")
                    sInstance = it
                }

            }
        }
    }
}
