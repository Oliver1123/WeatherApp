package com.oliver.weatherapp.data.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
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
    private final Context mContext;

    private static final Object LOCK = new Object();
    private static WeatherDataSource sInstance;
    private final MutableLiveData<WeatherEntry[]> mWeatherLiveData;

    public static WeatherDataSource getInstance(Context context, AppExecutors executors) {
        Log.d(TAG, "Get the WeatherDataSource");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new WeatherDataSource(context, executors);
                Log.d(TAG, "Made new WeatherDataSource");
            }
        }
        return sInstance;
    }

    private  WeatherDataSource(Context context, AppExecutors executors) {
        mExecutors = executors;
        mContext = context;

        mWeatherLiveData = new MutableLiveData<>();
    }

    public LiveData<WeatherEntry[]> getWeather() {
        return mWeatherLiveData;
    }

    public void startFetchWeatherService(long cityID, double latitude, double longitude) {
        SyncWeatherService.startSyncWeather(mContext, cityID, latitude, longitude);
    }

    public void fetchForecast(long cityID, double latitude, double longitude) {
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
