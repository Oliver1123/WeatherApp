package com.oliver.weatherapp.screens.home


import android.app.Activity.RESULT_OK
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker
import com.oliver.weatherapp.R
import com.oliver.weatherapp.domain.model.City
import com.oliver.weatherapp.screens.MainActivity
import com.oliver.weatherapp.screens.SelectedCitySharedViewModel
import com.oliver.weatherapp.screens.ViewModelFactory
import com.oliver.weatherapp.screens.base.BaseFragment
import com.oliver.weatherapp.utils.getAppComponent
import com.oliver.weatherapp.utils.getViewModel
import com.oliver.weatherapp.utils.observe
import kotlinx.android.synthetic.main.fragment_cities.*
import timber.log.Timber
import javax.inject.Inject

class CitiesFragment : BaseFragment() {
    private val PLACE_PICKER_REQUEST = 111
    private val KEY_FIST_VISIBLE_POSITION = "KEY_FIST_VISIBLE_POSITION"

    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: CitiesViewModel
    private lateinit var selectedCitySharedViewModel: SelectedCitySharedViewModel


    private lateinit var callback: CitiesFragmentInteractionListener

    private lateinit var rootView: View
    private lateinit var citiesAdapter: CitiesAdapter
    private var listPosition = RecyclerView.NO_POSITION
    private var citiesCount = 0




    private val mOnCityClickListener = object : CitiesAdapter.OnCityClickListener {
        override fun onDeleteClick(view: View, city: City, position: Int) {
            viewModel.deleteCity(city)
        }

        override fun onItemClick(view: View, city: City, position: Int) {
            selectedCitySharedViewModel.setCity(city)
            callback.onCitySelected(city)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            callback = context as CitiesFragmentInteractionListener
        } catch (exception: ClassCastException) {
            throw IllegalStateException(context?.javaClass?.simpleName + " must implement " + CitiesFragmentInteractionListener::class.java.simpleName)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAppComponent().inject(this)

        rootView = view
        initViewModel()

        initUI(view)
        restoreState(savedInstanceState)
    }

    private fun restoreState(savedInstanceState: Bundle?) {

        listPosition = savedInstanceState?.getInt(KEY_FIST_VISIBLE_POSITION, RecyclerView.NO_POSITION) ?: RecyclerView.NO_POSITION
    }

    private fun initUI(view: View) {
        initToolbar()
        fab_add_city.setOnClickListener{ this.addCityClick(it) }

        initRecyclerView(view.context)
    }

    private fun initToolbar() {
        val supportActionBar = (activity as AppCompatActivity).supportActionBar
        supportActionBar?.setTitle(R.string.cities_screen_title)
    }

    private fun initRecyclerView(context: Context) {
        citiesAdapter = CitiesAdapter(context, mOnCityClickListener)

        recycler_view_cities.layoutManager = LinearLayoutManager(context)
        recycler_view_cities.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recycler_view_cities.itemAnimator = DefaultItemAnimator()

        recycler_view_cities.adapter = citiesAdapter
    }

    private fun initViewModel() {
        // Get the ViewModel from the factory
        viewModel = getViewModel(factory)
        observe(viewModel.getCities(), this@CitiesFragment::onCitiesUpdated)

        selectedCitySharedViewModel = ViewModelProviders.of(activity as MainActivity, factory)
                .get(SelectedCitySharedViewModel::class.java)
    }

    // save scrolled position here, because onSaveInstanceState can be triggered after onDestroyView
    override fun onPause() {
        super.onPause()
        listPosition = (recycler_view_cities.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(KEY_FIST_VISIBLE_POSITION, listPosition)
    }

    private fun addCityClick(view: View) {
        val builder = PlacePicker.IntentBuilder()
        try {
            startActivityForResult(builder.build(activity!!), PLACE_PICKER_REQUEST)
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                val place = PlacePicker.getPlace(activity!!, data!!)
                handleSelectedPlace(place)
            }
        }
    }

    private fun handleSelectedPlace(place: Place) {
        val name = place.name.toString()
        val address= place.address?.toString()
        val latLng = place.latLng
        Timber.d("handleSelectedPlace: name: $name address $address latlng: $latLng")

        val city = City(name, address, latLng.latitude, latLng.longitude)
        viewModel.addCity(city)

        Snackbar.make(view!!, R.string.msg_place_saved, Snackbar.LENGTH_SHORT).show()
    }

    private fun onCitiesUpdated(cities: List<City>?) {
        Timber.d("onCitiesUpdated: cities: $cities")
        if (cities == null || cities.isEmpty()) {
            showEmptyListResult()
        } else {
            displayCities(cities)
        }
    }

    private fun showEmptyListResult() {
        tv_empty_list_message.visibility = View.VISIBLE
        recycler_view_cities.visibility = View.INVISIBLE
    }

    private fun displayCities(cities: List<City>) {
        tv_empty_list_message.visibility = View.INVISIBLE
        recycler_view_cities.visibility = View.VISIBLE

        citiesAdapter.setCities(cities)
        //        if new city was added, need to scroll list to start
        if (isNewCityAdded(cities.size)) {
            recycler_view_cities.smoothScrollToPosition(0)
        }
        tryToRestoreScrolledPosition(cities.size)
    }

    private fun tryToRestoreScrolledPosition(itemsCount: Int) {
        if (listPosition != RecyclerView.NO_POSITION && listPosition < itemsCount) {
            recycler_view_cities.smoothScrollToPosition(listPosition)
            listPosition = RecyclerView.NO_POSITION
        }
    }

    private fun isNewCityAdded(citiesCount: Int): Boolean {
        val result = citiesCount > this.citiesCount

        this.citiesCount = citiesCount
        return result
    }

    interface CitiesFragmentInteractionListener {
        fun onCitySelected(city: City)
    }

    companion object {

        fun newInstance(): CitiesFragment {

            val args = Bundle()

            val fragment = CitiesFragment()
            fragment.arguments = args
            Timber.d("newInstance: $fragment")
            return fragment
        }
    }
}
