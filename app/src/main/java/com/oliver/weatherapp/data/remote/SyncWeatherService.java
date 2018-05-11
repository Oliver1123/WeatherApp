package com.oliver.weatherapp.data.remote;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.oliver.weatherapp.Injector;

public class SyncWeatherService extends IntentService {
    private static final String EXTRA_CITY_ID = "EXTRA_CITY_ID";
    private static final String EXTRA_LATITUDE = "EXTRA_LATITUDE";
    private static final String EXTRA_LONGITUDE = "EXTRA_LONGITUDE";
    private static final String TAG = SyncWeatherService.class.getSimpleName();

    public SyncWeatherService() {
        super(TAG);
    }

    public static void startSyncWeather(Context context, long cityID, double latitude, double longitude) {
        Intent intent = new Intent(context, SyncWeatherService.class);
        intent.putExtra(EXTRA_CITY_ID, cityID);
        intent.putExtra(EXTRA_LATITUDE, latitude);
        intent.putExtra(EXTRA_LONGITUDE, longitude);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            long cityID = intent.getLongExtra(EXTRA_CITY_ID, 0);
            double latitude = intent.getDoubleExtra(EXTRA_LATITUDE, 0);
            double longitude = intent.getDoubleExtra(EXTRA_LONGITUDE, 0);

            WeatherDataSource dataSource = Injector.provideWeatherDataSource(this);
            dataSource.fetchForecast(cityID, latitude, longitude);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy " + this);
    }
}
