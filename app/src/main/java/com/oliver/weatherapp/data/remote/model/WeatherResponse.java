package com.oliver.weatherapp.data.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {
    @SerializedName("cod")
    public String code;
    @SerializedName("list")
    public List<DayWeather> dayWeatherList;
}
