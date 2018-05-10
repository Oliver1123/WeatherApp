package com.oliver.weatherapp.data.repositories;

import android.util.Log;

import com.oliver.weatherapp.AppExecutors;
import com.oliver.weatherapp.data.local.dao.CitiesDao;
import com.oliver.weatherapp.data.local.model.CityEntry;

import java.util.List;

public class CitiesRepository {

    private static final String TAG = CitiesRepository.class.getSimpleName();
    private final CitiesDao mCitiesDao;
    private final AppExecutors mExecutors;

    public CitiesRepository(AppExecutors executors, CitiesDao dao) {
        mCitiesDao = dao;
        mExecutors = executors;
    }

    public void addCity(CityEntry cityEntry) {
        Log.d(TAG, "addCity: " + cityEntry);
        mExecutors.diskIO().execute(() -> {
            mCitiesDao.insert(cityEntry);

            List<CityEntry> cities = mCitiesDao.getAll();
            Log.d(TAG, "addCity: cities: " + cities);
        });

    }
}
