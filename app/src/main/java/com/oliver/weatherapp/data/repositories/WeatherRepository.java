package com.oliver.weatherapp.data.repositories;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.oliver.weatherapp.AppExecutors;
import com.oliver.weatherapp.data.local.dao.WeatherDao;
import com.oliver.weatherapp.data.local.model.WeatherEntry;

import java.util.List;

public class WeatherRepository {

    private static final String TAG = WeatherRepository.class.getSimpleName();
    private final AppExecutors mExecutors;
    private final WeatherDao mWeatherDao;
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static WeatherRepository sInstance;

    public static WeatherRepository getInstance(AppExecutors executors, WeatherDao dao) {
        Log.d(TAG, "Get the WeatherRepository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new WeatherRepository(executors, dao);
                Log.d(TAG, "Made new WeatherRepository");
            }
        }
        return sInstance;
    }

    private WeatherRepository(AppExecutors executors, WeatherDao dao) {
        mExecutors = executors;
        mWeatherDao = dao;
    }

    public LiveData<List<WeatherEntry>> getForecast(long cityID) {
        return mWeatherDao.getForecast(cityID);
    }
}
