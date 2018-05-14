package com.oliver.weatherapp.data.remote.model

import com.google.gson.annotations.SerializedName

class WeatherResponse {
    @SerializedName("cod")
    var code: String? = null
    @SerializedName("list")
    var dayWeatherList: List<DayWeather>? = null
}

class DayWeather {
    @SerializedName("dt")
    var date: Long = 0
    @SerializedName("main")
    var main: MainInfo? = null
    @SerializedName("weather")
    var weather: List<WeatherStateInfo>? = null
    @SerializedName("wind")
    var wind: Wind? = null
    @SerializedName("rain")
    val rain: Rain? = null
}

class MainInfo {
    @SerializedName("temp_min")
    var tempMin: Double = 0.toDouble()
    @SerializedName("temp_max")
    var tempMax: Double = 0.toDouble()
    @SerializedName("humidity")
    var humidity: Long = 0
}

class Rain {
    @SerializedName("3h")
    var rainVolume: Double = 0.toDouble()
}

class WeatherStateInfo {
    @SerializedName("id")
    var id: Int = 0
}

class Wind {
    @SerializedName("speed")
    var speed: Double = 0.toDouble()
    @SerializedName("deg")
    var deg: Double = 0.toDouble()
}
