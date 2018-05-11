package com.oliver.weatherapp.screens.forecast;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.oliver.weatherapp.data.local.model.CityEntry;
import com.oliver.weatherapp.data.repositories.WeatherRepository;

public class WeatherViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final WeatherRepository mRepository;
    private final CityEntry mCity;

    public WeatherViewModelFactory(WeatherRepository repository, CityEntry cityID) {
        mRepository = repository;
        mCity = cityID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new WeatherViewModel(mRepository, mCity);
    }
}