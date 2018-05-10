package com.oliver.weatherapp.screens.home;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oliver.weatherapp.R;

public class CitiesFragment extends Fragment {

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
        Snackbar.make(view, "Hello Snackbar", Snackbar.LENGTH_SHORT).show();
    }
}
