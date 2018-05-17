package com.oliver.weatherapp.data.repositories

import com.oliver.weatherapp.AppExecutors
import com.oliver.weatherapp.data.local.dao.WeatherDao
import com.oliver.weatherapp.data.local.mapper.WeatherEntryToWeatherMapper
import com.oliver.weatherapp.data.remote.WeatherDataSource
import com.oliver.weatherapp.domain.model.WeatherItem
import com.oliver.weatherapp.domain.repositories.WeatherRepository
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*

class WeatherRepositoryImpl(
        private val executors: AppExecutors,
        private val weatherDao: WeatherDao,
        private val weatherDataSource: WeatherDataSource
) : WeatherRepository {
    private val isInitializedForCity = HashSet<Long>()

    init {
        observeNewWeatherData()
    }

    private fun observeNewWeatherData() {
        weatherDataSource.getWeather().observeForever { weatherEntries ->
            executors.diskIO().execute {
                Timber.d("observeNewWeatherData: ${weatherEntries?.size}")
                deleteOldData()

                weatherEntries?.let {
                    weatherDao.bulkInsert(*it)
                }

            }
        }
    }

    private fun deleteOldData() {
        val today = Date()
        Timber.d("deleteOldData: today: $today")
        weatherDao.deleteOldWeather(today)
    }

    override fun getForecast(cityID: Long, latitude: Double, longitude: Double): Flowable<List<WeatherItem>> {

        initialize(cityID, latitude, longitude)
        //        weatherDataSource.fetchForecast(cityID, latitude, longitude);
        return weatherDao.getForecast(cityID)
                .subscribeOn(Schedulers.io())
                .map(WeatherEntryToWeatherMapper())
    }


    private fun initialize(cityID: Long, latitude: Double, longitude: Double) {
        // initialize only once for instance and for city
        if (isInitializedForCity.contains(cityID)) return
        isInitializedForCity.add(cityID)

        executors.diskIO().execute {
            if (isFetchNeeded(cityID)) {
                weatherDataSource.fetchForecast(cityID, latitude, longitude)
            }
        }
    }

    /**
     * Need to update weather information if we don't have weather data for today + 5 days
     * if we have at least one record - don't update the weather information
     * @param cityID - id for the city to check cached forecast
     * @return true if we need to perform data update, false otherwise
     */
    private fun isFetchNeeded(cityID: Long): Boolean {
        val requiredDate = Calendar.getInstance()

        requiredDate.add(Calendar.DAY_OF_MONTH, 5)
        requiredDate.set(Calendar.HOUR_OF_DAY, 0)
        requiredDate.set(Calendar.MINUTE, 0)
        requiredDate.set(Calendar.SECOND, 0)
        requiredDate.set(Calendar.MILLISECOND, 0)

        val count = weatherDao.countFutureWeatherForCity(cityID, requiredDate.time)
        val isFetchNeeded = count == 0
        Timber.d("isFetchNeeded: $isFetchNeeded " +
                "requiredUpdateDate: ${requiredDate.time} " +
                "recordsCount: $count")
        return isFetchNeeded
    }

    override fun forceWeatherRefresh(cityID: Long, latitude: Double, longitude: Double) {
        executors.diskIO().execute {
            weatherDao.deleteWeatherForCity(cityID)
            weatherDataSource.fetchForecast(cityID, latitude, longitude)
        }
    }
}
