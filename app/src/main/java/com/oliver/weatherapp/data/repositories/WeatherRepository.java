package com.oliver.weatherapp.data.repositories;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.oliver.weatherapp.AppExecutors;
import com.oliver.weatherapp.data.local.dao.WeatherDao;
import com.oliver.weatherapp.data.local.model.WeatherEntry;
import com.oliver.weatherapp.data.remote.WeatherDataSource;

import java.util.Arrays;
import java.util.List;

public class WeatherRepository {

    private static final String TAG = WeatherRepository.class.getSimpleName();
    private final AppExecutors mExecutors;
    private final WeatherDao mWeatherDao;
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static WeatherRepository sInstance;
    private final WeatherDataSource mWeatherDataSource;

    public static WeatherRepository getInstance(AppExecutors executors,
                                                WeatherDao dao,
                                                WeatherDataSource weatherDataSource) {
        Log.d(TAG, "Get the WeatherRepository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new WeatherRepository(executors, dao, weatherDataSource);
                Log.d(TAG, "Made new WeatherRepository");
            }
        }
        return sInstance;
    }

    private WeatherRepository(AppExecutors executors,
                              WeatherDao dao,
                              WeatherDataSource weatherDataSource) {
        mExecutors = executors;
        mWeatherDao = dao;
        mWeatherDataSource = weatherDataSource;
        observeNewWeatherData();
    }

    private void observeNewWeatherData() {
        mWeatherDataSource.getWeather().observeForever(weatherEntries ->
                mExecutors.diskIO().execute(() -> {
                    Log.d(TAG, "observeNewWeatherData: " + Arrays.toString(weatherEntries));
                    mWeatherDao.bulkInsert(weatherEntries);
                }));

    }

    // TODO: 5/11/18 improve this, check if update needed, delete old data
    public LiveData<List<WeatherEntry>> getForecast(long cityID, double latitude, double longitude) {
        mWeatherDataSource.fetchForecast(cityID, latitude, longitude);
        return mWeatherDao.getForecast(cityID);
    }
}
