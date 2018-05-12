package com.oliver.weatherapp.data.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.google.gson.Gson;
import com.oliver.weatherapp.AppExecutors;
import com.oliver.weatherapp.data.local.model.WeatherEntry;
import com.oliver.weatherapp.data.remote.model.DayWeather;
import com.oliver.weatherapp.data.remote.model.WeatherResponse;

import java.net.URL;
import java.util.List;

public class WeatherDataSource {
    private static final String TAG = WeatherDataSource.class.getSimpleName();
    private final AppExecutors mExecutors;

    private static final Object LOCK = new Object();
    private static WeatherDataSource sInstance;
    private final MutableLiveData<WeatherEntry[]> mWeatherLiveData;

    public static WeatherDataSource getInstance(AppExecutors executors) {
        Log.d(TAG, "Get the WeatherDataSource");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new WeatherDataSource(executors);
                Log.d(TAG, "Made new WeatherDataSource");
            }
        }
        return sInstance;
    }

    private  WeatherDataSource(AppExecutors executors) {
        mExecutors = executors;

        mWeatherLiveData = new MutableLiveData<>();
    }

    public LiveData<WeatherEntry[]> getWeather() {
        return mWeatherLiveData;
    }

    public void fetchForecast(long cityID, double latitude, double longitude) {
        Log.d(TAG, "fetchForecast() called with: cityID = [" + cityID + "], (" + latitude + "," + longitude + ")");
        mExecutors.networkIO().execute(() -> {
            try {

                URL weatherRequestUrl = NetworkUtils.buildUrl(latitude, longitude);

                if (weatherRequestUrl == null) return;
                // Use the URL to retrieve the JSON
                String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);

                Gson gson = new Gson();
                WeatherResponse response = gson.fromJson(jsonWeatherResponse, WeatherResponse.class);

                if (response != null && response.dayWeatherList != null) {
                    DayWeatherToWeatherEntryMapper mapper = new DayWeatherToWeatherEntryMapper();
                    List<DayWeather> weatherList = response.dayWeatherList;

                    WeatherEntry[] weatherEntries = new WeatherEntry[weatherList.size()];
                    for (int i = 0; i < weatherList.size(); i++) {
                        WeatherEntry weatherEntry = mapper.apply(weatherList.get(i));
                        weatherEntry.cityID = cityID;
                        weatherEntries[i] = weatherEntry;
                    }
                    mWeatherLiveData.postValue(weatherEntries);
                }


            } catch (Exception e) {
                // Server probably invalid
                e.printStackTrace();
            }
        });
    }

}
