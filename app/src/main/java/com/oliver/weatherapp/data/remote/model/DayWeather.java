package com.oliver.weatherapp.data.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DayWeather {
    @SerializedName("dt")
    public long date;
    @SerializedName("main")
    public MainInfo main;
    @SerializedName("weather")
    public List<WeatherStateInfo> weather = null;
    @SerializedName("wind")
    public Wind wind;
    @SerializedName("rain")
    public Rain rain;
}
