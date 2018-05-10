package com.oliver.weatherapp;

import android.content.Context;

import com.oliver.weatherapp.data.local.WeatherDatabase;
import com.oliver.weatherapp.data.repositories.CitiesRepository;

public abstract class Injector {

    public static CitiesRepository provideCitiesRepository(Context context) {
        WeatherDatabase database = WeatherDatabase.getInstance(context);
        AppExecutors executors = AppExecutors.getInstance();
        return new CitiesRepository(executors, database.citiesDao());
    }
}
