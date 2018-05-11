package com.oliver.weatherapp;

import android.content.Context;

import com.oliver.weatherapp.data.local.WeatherDatabase;
import com.oliver.weatherapp.data.local.model.CityEntry;
import com.oliver.weatherapp.data.remote.WeatherDataSource;
import com.oliver.weatherapp.data.repositories.CitiesRepository;
import com.oliver.weatherapp.data.repositories.WeatherRepository;
import com.oliver.weatherapp.screens.forecast.WeatherViewModelFactory;
import com.oliver.weatherapp.screens.home.CitiesViewModelFactory;

public abstract class Injector {

    private static CitiesRepository provideCitiesRepository(Context context) {
        WeatherDatabase database = WeatherDatabase.getInstance(context);
        AppExecutors executors = AppExecutors.getInstance();
        return CitiesRepository.getInstance(executors, database.citiesDao());
    }

    private static WeatherRepository provideWeatherRepository(Context context) {
        WeatherDatabase database = WeatherDatabase.getInstance(context);
        AppExecutors executors = AppExecutors.getInstance();
        WeatherDataSource dataSource = provideWeatherDataSource();
        return WeatherRepository.getInstance(executors, database.weatherDao(), dataSource);
    }

    public static WeatherDataSource provideWeatherDataSource() {
        AppExecutors executors = AppExecutors.getInstance();
        return WeatherDataSource.getInstance(executors);
    }

    public static CitiesViewModelFactory provideCitiesViewModelFactory(Context context) {
        CitiesRepository repository = provideCitiesRepository(context.getApplicationContext());
        return new CitiesViewModelFactory(repository);
    }

    public static WeatherViewModelFactory provideWeatherViewModelFactory(Context context, CityEntry city) {
        WeatherRepository repository = provideWeatherRepository(context.getApplicationContext());
        return new WeatherViewModelFactory(repository, city);
    }
}
