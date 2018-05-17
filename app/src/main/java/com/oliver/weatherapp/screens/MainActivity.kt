package com.oliver.weatherapp.screens

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.Menu
import android.view.MenuItem
import com.oliver.weatherapp.R
import com.oliver.weatherapp.domain.model.City
import com.oliver.weatherapp.screens.base.BaseActivity
import com.oliver.weatherapp.screens.forecast.WeatherFragment
import com.oliver.weatherapp.screens.help.HelpFragment
import com.oliver.weatherapp.screens.home.CitiesFragment
import timber.log.Timber

class MainActivity : BaseActivity(), CitiesFragment.CitiesFragmentInteractionListener, FragmentManager.OnBackStackChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null)
            setFragment(CitiesFragment.newInstance())
        //Listen for changes in the back stack
        supportFragmentManager.addOnBackStackChangedListener(this)
        //Handle when activity is recreated like on orientation Change
        shouldDisplayHomeUp()
    }

    override fun onCitySelected(city: City) {
        Timber.d("onCitySelected: city: $city")
        if (shouldShowWeatherFragment(city)) {
            setFragment(WeatherFragment.newInstance(), true)
        }
    }

    // expand for two panel mode
    private fun shouldShowWeatherFragment(city: City): Boolean {
        return getCurrentFragment() !is WeatherFragment
    }

    private fun setFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(fragment.javaClass.simpleName)
        }
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_help -> showHelpFragment()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showHelpFragment() {
        if (getCurrentFragment() !is HelpFragment) {
            setFragment(HelpFragment.newInstance(), true)
        }
    }

    private fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(R.id.container)
    }

    override fun onBackStackChanged() {
        shouldDisplayHomeUp()
    }

    private fun shouldDisplayHomeUp() {
        //Enable Up button only  if there are entries in the back stack
        val canback = supportFragmentManager.backStackEntryCount > 0
        supportActionBar?.setDisplayHomeAsUpEnabled(canback)
    }

    override fun onSupportNavigateUp(): Boolean {
        //This method is called when the up button is pressed. Just the pop back stack.
        supportFragmentManager.popBackStack()
        return true
    }
}
