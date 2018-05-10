package com.oliver.weatherapp.screens.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.oliver.weatherapp.R;

import static android.app.Activity.RESULT_OK;

public class CitiesFragment extends Fragment {

    private static final int PLACE_PICKER_REQUEST = 111;
    private static final String TAG = CitiesFragment.class.getSimpleName();

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

        view.findViewById(R.id.fab_add_city).setOnClickListener(this::addCityClick);

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
        CharSequence name = place.getName();
        LatLng latLng = place.getLatLng();
        Log.d(TAG, "handleSelectedPlace: name: " + name + " latlng: " + latLng);
    }
}
