package com.oliver.weatherapp.screens.forecast;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.oliver.weatherapp.data.repositories.WeatherRepository;
import com.oliver.weatherapp.screens.home.WeatherViewModel;

public class WeatherViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final WeatherRepository mRepository;
    private final long mCityID;

    public WeatherViewModelFactory(WeatherRepository repository, long cityID) {
        mRepository = repository;
        mCityID = cityID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new WeatherViewModel(mRepository, mCityID);
    }
}