package com.oliver.weatherapp.data.repositories;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.oliver.weatherapp.AppExecutors;
import com.oliver.weatherapp.data.local.dao.CitiesDao;
import com.oliver.weatherapp.data.local.model.CityEntry;

import java.util.List;

public class CitiesRepository {

    private static final String TAG = CitiesRepository.class.getSimpleName();
    private final CitiesDao mCitiesDao;
    private final AppExecutors mExecutors;
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static CitiesRepository sInstance;

    public static CitiesRepository getInstance(AppExecutors executors, CitiesDao dao) {
        Log.d(TAG, "Get the CitiesRepository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new CitiesRepository(executors, dao);
                Log.d(TAG, "Made new CitiesRepository");
            }
        }
        return sInstance;
    }

    private CitiesRepository(AppExecutors executors, CitiesDao dao) {
        mCitiesDao = dao;
        mExecutors = executors;
    }

    public void addCity(CityEntry cityEntry) {
        Log.d(TAG, "addCity: " + cityEntry);
        mExecutors.diskIO().execute(() -> mCitiesDao.insert(cityEntry));
    }

    public LiveData<List<CityEntry>> getCities() {
        return mCitiesDao.getAll();
    }

    public void deleteCity(CityEntry city) {
        mExecutors.diskIO().execute(() -> mCitiesDao.deleteCity(city.id));
    }
}
