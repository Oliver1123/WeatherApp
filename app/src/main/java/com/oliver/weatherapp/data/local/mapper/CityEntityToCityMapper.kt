package com.oliver.weatherapp.data.local.mapper

import com.oliver.weatherapp.data.local.model.CityEntry
import com.oliver.weatherapp.domain.model.City
import io.reactivex.functions.Function

class CityEntityToCityMapper : Function<List<CityEntry>, List<City>> {

    override fun apply(cities: List<CityEntry>): List<City> {
        return cities.map {
            it.run {
                City(id, name, address, latitude, longitude)
            }

        }
    }

}