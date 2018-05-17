package com.oliver.weatherapp.domain.repositories

import com.oliver.weatherapp.domain.model.WeatherItem
import io.reactivex.Flowable

interface WeatherRepository {

    fun getForecast(cityID: Long, latitude: Double, longitude: Double): Flowable<List<WeatherItem>>

    fun forceWeatherRefresh(cityID: Long, latitude: Double, longitude: Double)
}
