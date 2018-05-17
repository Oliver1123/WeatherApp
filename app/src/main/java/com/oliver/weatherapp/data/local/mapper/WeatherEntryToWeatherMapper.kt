package com.oliver.weatherapp.data.local.mapper

import com.oliver.weatherapp.data.local.model.WeatherEntry
import com.oliver.weatherapp.domain.model.WeatherItem
import io.reactivex.functions.Function

class WeatherEntryToWeatherMapper : Function<List<WeatherEntry>, List<WeatherItem>> {

    override fun apply(forecast: List<WeatherEntry>): List<WeatherItem> {
        return forecast.map {
            it.run {
                WeatherItem(cityID, weatherIconId, date, min, max,
                        humidity, wind, degrees, rainVolume)
            }

        }
    }

}