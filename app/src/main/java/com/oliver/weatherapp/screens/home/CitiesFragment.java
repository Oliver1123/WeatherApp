package com.oliver.weatherapp.screens.home;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.oliver.weatherapp.Injector;
import com.oliver.weatherapp.R;
import com.oliver.weatherapp.data.local.model.CityEntry;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class CitiesFragment extends Fragment {

    private static final int PLACE_PICKER_REQUEST = 111;
    private static final String TAG = CitiesFragment.class.getSimpleName();
    private CitiesViewModel mViewModel;
    private TextView mEmptyListMessage;
    private RecyclerView mCitiesRecyclerView;
    private CitiesAdapter mCitiesAdapter;

    private CitiesAdapter.OnCityClickListener mOnCityClickListener = new CitiesAdapter.OnCityClickListener() {
        @Override
        public void onDeleteClick(View view, CityEntry city, int position) {
            mViewModel.deleteCity(city);
        }

        @Override
        public void onItemClick(View view, CityEntry city, int position) {

        }
    };
    private int mCitiesCount = 0;

    public static CitiesFragment newInstance() {

        Bundle args = new Bundle();

        CitiesFragment fragment = new CitiesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewModel();

        initUI(view);
    }

    private void initUI(@NonNull View view) {
        view.findViewById(R.id.fab_add_city).setOnClickListener(this::addCityClick);
        mEmptyListMessage = view.findViewById(R.id.tv_empty_list_message);
        mCitiesRecyclerView = view.findViewById(R.id.recycler_view_cities);

        initRecyclerView();
    }

    private void initRecyclerView() {
        mCitiesAdapter = new CitiesAdapter(getContext(), mOnCityClickListener);

        mCitiesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mCitiesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        mCitiesRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mCitiesRecyclerView.setAdapter(mCitiesAdapter);
    }

    private void initViewModel() {
        // Get the ViewModel from the factory
        CitiesViewModelFactory factory = Injector.provideCitiesViewModelFactory(getContext().getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(CitiesViewModel.class);
        mViewModel.getCities().observe(this, this::onCitiesUpdated);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO: 5/10/18 implement save instance state
    }

    private void addCityClick(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);
                handleSelectedPlace(place);
            }
        }
    }

    private void handleSelectedPlace(Place place) {
        String name = place.getName().toString();
        String address = (place.getAddress() != null) ?
                place.getAddress().toString() : "";
        LatLng latLng = place.getLatLng();
        Log.d(TAG, "handleSelectedPlace: name: " + name + " address " + address + " latlng: " + latLng);

        CityEntry cityEntry = new CityEntry(name, address, latLng.latitude, latLng. longitude);
        mViewModel.addCity(cityEntry);
    }

    private void onCitiesUpdated(List<CityEntry> cities) {
        Log.d(TAG, "onCitiesUpdated: cities: " + cities);
        if (cities == null || cities.isEmpty()) {
            showEmptyListResult();
        } else {
            displayCities(cities);
        }
    }

    private void showEmptyListResult() {
        mEmptyListMessage.setVisibility(View.VISIBLE);
        mCitiesRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void displayCities(@NonNull List<CityEntry> cities) {
        mEmptyListMessage.setVisibility(View.INVISIBLE);
        mCitiesRecyclerView.setVisibility(View.VISIBLE);

        mCitiesAdapter.setCities(cities);
//        if new city was added, need to scroll list to start
        if (isCityAdded(cities.size())) {
            mCitiesRecyclerView.smoothScrollToPosition(0);
        }
    }

    private boolean isCityAdded(int citiesCount) {
        boolean result = false;
        if (citiesCount > mCitiesCount) {
            result = true;
        }
        mCitiesCount = citiesCount;
        return result;
    }
}
