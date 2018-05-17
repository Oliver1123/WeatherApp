package com.oliver.weatherapp.domain.repositories

import com.oliver.weatherapp.domain.model.City
import io.reactivex.Flowable

interface CitiesRepository {

    fun getCities(): Flowable<List<City>>

    fun addCity(city: City)

    fun deleteCity(cityId: Long)
}