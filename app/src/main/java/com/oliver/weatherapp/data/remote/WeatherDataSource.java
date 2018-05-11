package com.oliver.weatherapp.data.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.oliver.weatherapp.AppExecutors;
import com.oliver.weatherapp.data.local.model.WeatherEntry;
import com.oliver.weatherapp.data.remote.model.DayWeather;
import com.oliver.weatherapp.data.remote.model.WeatherResponse;

import java.net.URL;

public class WeatherDataSource {
    public static final int NUM_DAYS_FORECAST = 5;
    private static final String TAG = WeatherDataSource.class.getSimpleName();
    private final AppExecutors mExecutors;

    public WeatherDataSource(AppExecutors executors) {
        mExecutors = executors;
    }

    public void fetchForecast(double latitude, double longitude) {
        mExecutors.networkIO().execute(() -> {
            try {

                URL weatherRequestUrl = NetworkUtils.buildUrl(latitude, longitude, NUM_DAYS_FORECAST);

                if (weatherRequestUrl == null) return;
                // Use the URL to retrieve the JSON
                String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
                Log.d(TAG, "fetchForecast: json: " + jsonWeatherResponse);
                Gson gson = new Gson();

                WeatherResponse response = gson.fromJson(jsonWeatherResponse, WeatherResponse.class);
                DayWeatherToWeatherEntryMapper mapper = new DayWeatherToWeatherEntryMapper();
                if (response != null && response.dayWeatherList != null) {
                    for (DayWeather dayWeather : response.dayWeatherList) {
                        WeatherEntry weatherEntry = mapper.apply(dayWeather);
                        Log.d(TAG, "weatherEntry: " + weatherEntry);
                    }
                }
                // TODO: 5/11/18 handle forecast results here

            } catch (Exception e) {
                // Server probably invalid
                e.printStackTrace();
            }
        });
    }
}
