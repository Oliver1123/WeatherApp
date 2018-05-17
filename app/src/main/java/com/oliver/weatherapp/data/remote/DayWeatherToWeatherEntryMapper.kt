package com.oliver.weatherapp.data.remote

import com.oliver.weatherapp.data.local.model.WeatherEntry
import com.oliver.weatherapp.data.remote.model.DayWeather
import com.oliver.weatherapp.data.remote.model.WeatherResponse
import io.reactivex.functions.Function
import java.util.*

class WeatherResponseToWeatherEntryListMapper(
        private val cityID: Long
) : Function<WeatherResponse, List<WeatherEntry>?> {
    private val itemMapper = DayWeatherToWeatherEntryMapper()

    override fun apply(response: WeatherResponse): List<WeatherEntry>? {
        return response.dayWeatherList?.map {
            itemMapper.apply(it)
                    .also { weatherEntry -> weatherEntry.cityID = cityID  }
        }
    }
}

class DayWeatherToWeatherEntryMapper {

    fun apply(dayWeather: DayWeather): WeatherEntry {
        val entry = WeatherEntry()

        dayWeather.weather?.let {
            if (it.isNotEmpty())
                entry.weatherIconId = it[0].id
        }
        entry.date = Date(dayWeather.date * 1000)

        dayWeather.main?.let {
            entry.min = it.tempMin
            entry.max = it.tempMax
            entry.humidity = it.humidity.toDouble()
        }

        dayWeather.wind?.let {
            entry.wind = it.speed
            entry.degrees = it.deg
        }

        dayWeather.rain?.let {
            entry.rainVolume = it.rainVolume
        }
        return entry
    }
}
