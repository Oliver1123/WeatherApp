package com.oliver.weatherapp.data.repositories;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.oliver.weatherapp.AppExecutors;
import com.oliver.weatherapp.data.local.dao.WeatherDao;
import com.oliver.weatherapp.data.local.model.WeatherEntry;
import com.oliver.weatherapp.data.remote.WeatherDataSource;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WeatherRepository {

    private static final String TAG = WeatherRepository.class.getSimpleName();
    private final AppExecutors mExecutors;
    private final WeatherDao mWeatherDao;
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static WeatherRepository sInstance;
    private final WeatherDataSource mWeatherDataSource;
    private Set<Long> mInitializedForCity = new HashSet<>();

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
                    Log.d(TAG, "observeNewWeatherData: " + (weatherEntries != null ? weatherEntries.length : 0));
                    deleteOldData();

                    mWeatherDao.bulkInsert(weatherEntries);
                }));

    }

    private void deleteOldData() {
        Date today = new Date();
        Log.d(TAG, "deleteOldData: today: " + today);
        mWeatherDao.deleteOldWeather(today);
    }

    public LiveData<List<WeatherEntry>> getForecast(long cityID, double latitude, double longitude) {

        initialize(cityID, latitude, longitude);
//        mWeatherDataSource.fetchForecast(cityID, latitude, longitude);
        return mWeatherDao.getForecast(cityID);
    }


    private void initialize(long cityID, double latitude, double longitude) {
        // initialize only once for instance and for city
        if (mInitializedForCity.contains(cityID)) return;
        mInitializedForCity.add(cityID);

        mExecutors.diskIO().execute(() -> {
            if (isFetchNeeded(cityID)) {
                mWeatherDataSource.fetchForecast(cityID, latitude, longitude);
            }
        });
    }

    /**
     * Need to update weather information if we don't have weather data for today + 5 days
     * if we have at least one record - don't update the weather information
     * @param cityID - id for the city to check cached forecast
     * @return true if we need to perform data update, false otherwise
     */
    private boolean isFetchNeeded(long cityID) {
        Calendar requiredDate = Calendar.getInstance();

        requiredDate.add(Calendar.DAY_OF_MONTH, 5);
        requiredDate.set(Calendar.HOUR_OF_DAY, 0);
        requiredDate.set(Calendar.MINUTE, 0);
        requiredDate.set(Calendar.SECOND, 0);
        requiredDate.set(Calendar.MILLISECOND, 0);

        int count = mWeatherDao.countFutureWeatherForCity(cityID, requiredDate.getTime());
        boolean isFetchNeeded = count == 0;
        Log.d(TAG, "isFetchNeeded: " + isFetchNeeded +
                " requiredUpdateDate: " + requiredDate.getTime() +
                " recordsCount: " + count);
        return isFetchNeeded;
    }
}
