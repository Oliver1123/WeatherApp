package com.oliver.weatherapp.screens.forecast;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.oliver.weatherapp.data.local.model.CityEntry;
import com.oliver.weatherapp.data.local.model.WeatherEntry;
import com.oliver.weatherapp.data.repositories.WeatherRepository;

import java.util.List;

public class WeatherViewModel extends ViewModel {

    private static final String TAG = WeatherViewModel.class.getSimpleName();
    private final WeatherRepository mRepository;
    private final LiveData<List<WeatherEntry>> mForecast;

    public WeatherViewModel(WeatherRepository repository, CityEntry city) {
        Log.d(TAG, "WeatherViewModel: constructor repository: " + repository);
        mRepository = repository;
        mForecast = mRepository.getForecast(city.id, city.latitude, city.longitude);
    }

    public LiveData<List<WeatherEntry>> getForecast() {
        return mForecast;
    }
}
