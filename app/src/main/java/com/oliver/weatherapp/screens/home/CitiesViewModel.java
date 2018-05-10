package com.oliver.weatherapp.screens.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.oliver.weatherapp.data.local.model.CityEntry;
import com.oliver.weatherapp.data.repositories.CitiesRepository;

import java.util.List;

public class CitiesViewModel extends ViewModel {

    private static final String TAG = CitiesViewModel.class.getSimpleName();
    private final CitiesRepository mRepository;
    private final LiveData<List<CityEntry>> mCities;

    public CitiesViewModel(CitiesRepository repository) {
        Log.d(TAG, "CitiesViewModel: constructor repository: " + repository);
        mRepository = repository;
        mCities = mRepository.getCities();
    }

    public void addCity(CityEntry cityEntry) {
        mRepository.addCity(cityEntry);
    }

    public LiveData<List<CityEntry>> getCities() {
        return mCities;
    }

    public void deleteCity(CityEntry city) {
        mRepository.deleteCity(city);
    }
}
