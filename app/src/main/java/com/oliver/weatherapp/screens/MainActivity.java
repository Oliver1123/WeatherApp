package com.oliver.weatherapp.screens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.oliver.weatherapp.R;
import com.oliver.weatherapp.data.local.model.CityEntry;
import com.oliver.weatherapp.screens.forecast.WeatherFragment;
import com.oliver.weatherapp.screens.help.HelpFragment;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_help) {
            showHelpFragment();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHelpFragment() {
        if (!TextUtils.equals(HelpFragment.class.getSimpleName(), getCurrentFragmentName())) {
            setFragment(HelpFragment.newInstance(), true);
        }
    }

    private String getCurrentFragmentName() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment != null)
            return fragment.getClass().getSimpleName();

        return null;
    }
}
