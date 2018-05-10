package com.oliver.weatherapp.screens.home;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.oliver.weatherapp.data.repositories.CitiesRepository;

public class CitiesViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final CitiesRepository mRepository;

    public CitiesViewModelFactory(CitiesRepository repository) {
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new CitiesViewModel(mRepository);
    }
}