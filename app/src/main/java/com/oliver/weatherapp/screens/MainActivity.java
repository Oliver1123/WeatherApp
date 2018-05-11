package com.oliver.weatherapp.screens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.oliver.weatherapp.R;
import com.oliver.weatherapp.data.local.model.CityEntry;
import com.oliver.weatherapp.screens.forecast.WeatherFragment;
import com.oliver.weatherapp.screens.home.CitiesFragment;

public class MainActivity extends AppCompatActivity implements CitiesFragment.CitiesFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFragment(restoreFragment(), false);
    }

    private Fragment restoreFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            // nothing to restore, display Cities
            fragment = CitiesFragment.newInstance();
        }
        return fragment;
    }

    private void setFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.commit();
    }

    @Override
    public void onCitySelected(CityEntry city) {
        setFragment(WeatherFragment.newInstance(city), true);
    }
}
