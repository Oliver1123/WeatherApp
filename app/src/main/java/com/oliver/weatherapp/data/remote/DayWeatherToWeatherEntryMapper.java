package com.oliver.weatherapp.data.remote;

import com.oliver.weatherapp.data.local.model.WeatherEntry;
import com.oliver.weatherapp.data.remote.model.DayWeather;

import java.util.Date;

public class DayWeatherToWeatherEntryMapper  {

    public WeatherEntry apply(DayWeather dayWeather) {
        WeatherEntry entry = new WeatherEntry();
        if (dayWeather.weather != null && !dayWeather.weather.isEmpty()) {
            entry.weatherIconId = dayWeather.weather.get(0).id;
        }
        entry.date = new Date(dayWeather.date * 1000);
        if (dayWeather.main != null) {
            entry.min = dayWeather.main.tempMin;
            entry.max = dayWeather.main.tempMax;
            entry.humidity = dayWeather.main.humidity;
        }
        if (dayWeather.wind != null) {
            entry.wind = dayWeather.wind.speed;
            entry.degrees = dayWeather.wind.deg;
        }

        if (dayWeather.rain != null) {
            entry.rainVolume = dayWeather.rain.rainVolume;
        }
        return entry;
    }
}
