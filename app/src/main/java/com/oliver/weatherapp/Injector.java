package com.oliver.weatherapp;

import android.content.Context;

import com.oliver.weatherapp.data.local.WeatherDatabase;
import com.oliver.weatherapp.data.remote.WeatherDataSource;
import com.oliver.weatherapp.data.repositories.CitiesRepository;
import com.oliver.weatherapp.data.repositories.WeatherRepository;
import com.oliver.weatherapp.screens.forecast.WeatherViewModelFactory;
import com.oliver.weatherapp.screens.home.CitiesViewModelFactory;

public abstract class Injector {

    public static CitiesRepository provideCitiesRepository(Context context) {
        WeatherDatabase database = WeatherDatabase.getInstance(context);
        AppExecutors executors = AppExecutors.getInstance();
        return CitiesRepository.getInstance(executors, database.citiesDao());
    }

    public static WeatherRepository provideWeatherRepository(Context context) {
        WeatherDatabase database = WeatherDatabase.getInstance(context);
        AppExecutors executors = AppExecutors.getInstance();
        return WeatherRepository.getInstance(executors, database.weatherDao());
    }

    public static WeatherDataSource provideWeatherDataSource(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        return WeatherDataSource.getInstance(context, executors);
    }

    public static CitiesViewModelFactory provideCitiesViewModelFactory(Context context) {
        CitiesRepository repository = provideCitiesRepository(context.getApplicationContext());
        return new CitiesViewModelFactory(repository);
    }

    public static WeatherViewModelFactory provideWeatherViewModelFactory(Context context, long cityID) {
        WeatherRepository repository = provideWeatherRepository(context.getApplicationContext());
        return new WeatherViewModelFactory(repository, cityID);
    }
}
