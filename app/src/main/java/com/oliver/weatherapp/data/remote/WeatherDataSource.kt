package com.oliver.weatherapp.data.remote

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.oliver.weatherapp.AppExecutors
import com.oliver.weatherapp.data.local.model.WeatherEntry
import com.oliver.weatherapp.data.remote.model.WeatherResponse
import timber.log.Timber

class WeatherDataSource private constructor(private val mExecutors: AppExecutors) {
    private val weatherLiveData: MutableLiveData<Array<WeatherEntry>> = MutableLiveData()

    fun getWeather() = weatherLiveData

    fun fetchForecast(cityID: Long, latitude: Double, longitude: Double) {
        Timber.d("fetchForecast cityID: $cityID, ($latitude, $longitude)")
        mExecutors.networkIO().execute {
            try {

                val weatherRequestUrl = NetworkUtils.buildUrl(latitude, longitude) ?: return@execute

                // Use the URL to retrieve the JSON
                val jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl)

                val gson = Gson()
                val response = gson.fromJson(jsonWeatherResponse, WeatherResponse::class.java)

                val mapper = DayWeatherToWeatherEntryMapper()

                val mappedWeather = response?.dayWeatherList?.map {
                    mapper.apply(it).also { it.cityID = cityID }
                }
                weatherLiveData.postValue(mappedWeather?.toTypedArray())

            } catch (e: Exception) {
                // Server probably invalid
                e.printStackTrace()
            }
        }
    }

    companion object {

        private val LOCK = Any()
        @Volatile
        private var sInstance: WeatherDataSource? = null

        fun getInstance(executors: AppExecutors): WeatherDataSource {
            Timber.d("Get the WeatherDataSource")
            return sInstance ?: synchronized(LOCK) {
                sInstance ?: WeatherDataSource(executors).also {
                    Timber.d("Made new WeatherDataSource")
                    sInstance = it
                }
            }
        }
    }

}
