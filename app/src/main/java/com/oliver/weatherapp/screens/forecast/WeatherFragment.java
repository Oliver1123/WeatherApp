package com.oliver.weatherapp.screens.forecast;


import android.arch.lifecycle.ViewModelProviders;
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

import com.oliver.weatherapp.Injector;
import com.oliver.weatherapp.R;
import com.oliver.weatherapp.data.local.model.CityEntry;
import com.oliver.weatherapp.data.local.model.WeatherEntry;
import com.oliver.weatherapp.screens.home.WeatherViewModel;

import java.util.List;

public class WeatherFragment extends Fragment {
    
    private static final String TAG = WeatherFragment.class.getSimpleName();
    private static final String ARG_CITY = "ARG_CITY";

    private WeatherViewModel mViewModel;
    private TextView mEmptyListMessage;
    private RecyclerView mWeatherRecyclerView;
    private WeatherAdapter mWeatherAdapter;

    public static WeatherFragment newInstance(CityEntry city) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_CITY, city);
        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewModel();

        initUI(view);
    }

    private void initUI(@NonNull View view) {
        mEmptyListMessage = view.findViewById(R.id.tv_empty_list_message);
        mWeatherRecyclerView = view.findViewById(R.id.recycler_view_forecast);

        initRecyclerView();
    }

    private void initRecyclerView() {
        mWeatherAdapter = new WeatherAdapter(getContext());

        mWeatherRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mWeatherRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        mWeatherRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mWeatherRecyclerView.setAdapter(mWeatherAdapter);
    }

    private void initViewModel() {
        // Get the ViewModel from the factory
        CityEntry city = getArguments().getParcelable(ARG_CITY);
        WeatherViewModelFactory factory = Injector.provideWeatherViewModelFactory(getContext().getApplicationContext(), city);
        mViewModel = ViewModelProviders.of(this, factory).get(WeatherViewModel.class);
        mViewModel.getForecast().observe(this, this::onWeatherUpdated);
    }

    private void onWeatherUpdated(List<WeatherEntry> forecast) {
        Log.d(TAG, "onWeatherUpdated: Weather: " + forecast);
        if (forecast == null || forecast.isEmpty()) {
            showEmptyListResult();
        } else {
            displayWeather(forecast);
        }
    }

    private void showEmptyListResult() {
        mEmptyListMessage.setVisibility(View.VISIBLE);
        mWeatherRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void displayWeather(@NonNull List<WeatherEntry> Weather) {
        mEmptyListMessage.setVisibility(View.INVISIBLE);
        mWeatherRecyclerView.setVisibility(View.VISIBLE);

        mWeatherAdapter.setForecast(Weather);
    }
}