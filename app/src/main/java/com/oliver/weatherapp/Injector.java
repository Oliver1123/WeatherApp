package com.oliver.weatherapp;

import android.content.Context;

import com.oliver.weatherapp.data.local.WeatherDatabase;
import com.oliver.weatherapp.data.repositories.CitiesRepository;
import com.oliver.weatherapp.screens.home.CitiesViewModelFactory;

public abstract class Injector {

    public static CitiesRepository provideCitiesRepository(Context context) {
        WeatherDatabase database = WeatherDatabase.getInstance(context);
        AppExecutors executors = AppExecutors.getInstance();
        return CitiesRepository.getInstance(executors, database.citiesDao());
    }


    public static CitiesViewModelFactory provideCitiesViewModelFactory(Context context) {
        CitiesRepository repository = provideCitiesRepository(context.getApplicationContext());
        return new CitiesViewModelFactory(repository);
    }
}
