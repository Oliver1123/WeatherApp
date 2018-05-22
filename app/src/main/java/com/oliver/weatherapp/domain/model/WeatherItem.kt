package com.oliver.weatherapp.domain.model

import java.util.*

data class WeatherItem(
        val cityID: Long = 0,
        val weatherIconId: Int = 0,
        val date: Date = Date(0),
        val min: Double = 0.0,
        val max: Double = 0.0,
        val humidity: Double = 0.0,
        val wind: Double = 0.0,
        val degrees: Double = 0.0,
        val rainVolume: Double = 0.0
) {

    fun areTheSame(entry: WeatherItem): Boolean {
        return cityID == entry.cityID && date == entry.date
    }
}
