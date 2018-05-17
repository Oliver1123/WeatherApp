package com.oliver.weatherapp.data.remote

import com.oliver.weatherapp.BuildConfig
import com.oliver.weatherapp.data.remote.model.WeatherResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

const val PATH = "/data/2.5/forecast?appid=${BuildConfig.API_KEY}&units=metric"

interface WeatherApi {

    //http://api.openweathermap.org/data/2.5/forecast?lat=0&lon=0&appid=c6e381d8c7ff98f0fee43775817cf6ad&units=metric
    @GET(PATH)
    fun getForecast(@Query("lat") latitude: Double, @Query("lon") longitude: Double): Single<WeatherResponse>

}