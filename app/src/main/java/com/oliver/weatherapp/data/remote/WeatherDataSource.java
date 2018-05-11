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
    public static final int NUM_DAYS_FORECAST = 5;
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

                URL weatherRequestUrl = NetworkUtils.buildUrl(latitude, longitude, NUM_DAYS_FORECAST);

                if (weatherRequestUrl == null) return;
                // Use the URL to retrieve the JSON
                String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
                Log.d(TAG, "fetchForecast: json: " + jsonWeatherResponse);


                // TODO: 5/11/18 remove this before release, need to check how it will work on slow connection
                Thread.sleep(5000);
                
                Gson gson = new Gson();
                WeatherResponse response = gson.fromJson(jsonWeatherResponse, WeatherResponse.class);

/*
                forecast return 5 record, but no guarantee that they will be for 5 different days
                    WeatherEntry{cityId=1, weatherIconId=800, date=Thu May 10 21:00:00 GMT+03:00 2018
                    WeatherEntry{cityId=1, weatherIconId=800, date=Fri May 11 00:00:00 GMT+03:00 2018
                    WeatherEntry{cityId=1, weatherIconId=800, date=Fri May 11 03:00:00 GMT+03:00 2018
                    WeatherEntry{cityId=1, weatherIconId=800, date=Fri May 11 06:00:00 GMT+03:00 2018
                    WeatherEntry{cityId=1, weatherIconId=800, date=Fri May 11 09:00:00 GMT+03:00 2018
*/
                if (response != null && response.dayWeatherList != null) {
                    DayWeatherToWeatherEntryMapper mapper = new DayWeatherToWeatherEntryMapper();
                    List<DayWeather> weatherList = response.dayWeatherList;

                    WeatherEntry[] weatherEntries = new WeatherEntry[weatherList.size()];
                    for (int i = 0; i < weatherList.size(); i++) {
                        WeatherEntry weatherEntry = mapper.apply(weatherList.get(i));
                        weatherEntry.cityID = cityID;
                        weatherEntries[i] = weatherEntry;
                        Log.d(TAG, "weatherEntry: " + weatherEntry);
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
