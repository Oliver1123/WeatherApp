package com.oliver.weatherapp.data.repositories

import com.oliver.weatherapp.AppExecutors
import com.oliver.weatherapp.data.local.dao.CitiesDao
import com.oliver.weatherapp.data.local.mapper.CityEntityToCityMapper
import com.oliver.weatherapp.data.local.model.CityEntry
import com.oliver.weatherapp.domain.model.City
import com.oliver.weatherapp.domain.repositories.CitiesRepository
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class CitiesRepositoryImpl(
    private val executors: AppExecutors,
    private val citiesDao: CitiesDao
) : CitiesRepository {


    override fun getCities(): Flowable<List<City>> =
        citiesDao.getAll()
            .map(CityEntityToCityMapper())
            .subscribeOn(Schedulers.io())

    override fun addCity(city: City) {
        Timber.d("addCity: $city")
        city.apply {
            val cityEntry = CityEntry(name, address, latitude, longitude)
            executors.diskIO().execute { citiesDao.insert(cityEntry) }
        }
    }

    override fun deleteCity(cityId: Long) {
        executors.diskIO().execute { citiesDao.deleteCity(cityId) }
    }
}
