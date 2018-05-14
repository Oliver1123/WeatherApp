package com.oliver.weatherapp.screens

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.oliver.weatherapp.R
import com.oliver.weatherapp.data.local.model.CityEntry
import com.oliver.weatherapp.screens.forecast.WeatherFragment
import com.oliver.weatherapp.screens.help.HelpFragment
import com.oliver.weatherapp.screens.home.CitiesFragment

class MainActivity : AppCompatActivity(), CitiesFragment.CitiesFragmentInteractionListener, FragmentManager.OnBackStackChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFragment(restoreFragment(), false)
        //Listen for changes in the back stack
        supportFragmentManager.addOnBackStackChangedListener(this)
        //Handle when activity is recreated like on orientation Change
        shouldDisplayHomeUp()
    }

    private fun getCurrentFragmentName(): String? {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        return fragment?.javaClass?.simpleName
    }


    private fun restoreFragment(): Fragment {
        val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.container)

        return fragment ?: CitiesFragment.newInstance()
    }

    private fun setFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(fragment.javaClass.simpleName)
        }
        transaction.commit()
    }

    override fun onCitySelected(city: CityEntry) {
        setFragment(WeatherFragment.newInstance(city), true)
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
        if (HelpFragment::class.java.simpleName != getCurrentFragmentName()) {
            setFragment(HelpFragment.newInstance(), true)
        }
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
