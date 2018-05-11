package com.oliver.weatherapp.screens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.oliver.weatherapp.Injector;
import com.oliver.weatherapp.R;
import com.oliver.weatherapp.data.remote.WeatherDataSource;
import com.oliver.weatherapp.screens.forecast.WeatherFragment;
import com.oliver.weatherapp.screens.home.CitiesFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFragment(getWeatherFragment());
        // TODO: 5/11/18 remove this after forecast fetch process will be established
        tmp();
    }

    private void tmp() {
        WeatherDataSource dataSource = Injector.provideWeatherDataSource(this.getApplicationContext());
        dataSource.startFetchWeatherService(0, 0, 0);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    private CitiesFragment getCitiesFragment() {
        CitiesFragment fragment =
                (CitiesFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = CitiesFragment.newInstance();
        }
        return fragment;
    }

    private WeatherFragment getWeatherFragment() {
        WeatherFragment fragment =
                (WeatherFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = WeatherFragment.newInstance(0);
        }
        return fragment;
    }
}
