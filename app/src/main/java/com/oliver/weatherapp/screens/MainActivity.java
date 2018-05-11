package com.oliver.weatherapp.screens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.oliver.weatherapp.R;
import com.oliver.weatherapp.data.local.model.CityEntry;
import com.oliver.weatherapp.screens.forecast.WeatherFragment;
import com.oliver.weatherapp.screens.home.CitiesFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFragment(getWeatherFragment());

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

    // TODO: 5/11/18 implement fragments replace
    private WeatherFragment getWeatherFragment() {
        WeatherFragment fragment =
                (WeatherFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = WeatherFragment.newInstance(new CityEntry("cityName", "cityaddress", 0, 0));
        }
        return fragment;
    }
}
