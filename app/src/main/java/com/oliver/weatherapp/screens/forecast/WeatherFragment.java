package com.oliver.weatherapp.screens.forecast;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

import java.util.List;

public class WeatherFragment extends Fragment {
    
    private static final String TAG = WeatherFragment.class.getSimpleName();
    private static final String ARG_CITY = "ARG_CITY";
    private static final String KEY_FIST_VISIBLE_POSITION = "KEY_FIST_VISIBLE_POSITION";

    private WeatherViewModel mViewModel;
    private TextView mEmptyListMessage;
    private RecyclerView mWeatherRecyclerView;
    private WeatherAdapter mWeatherAdapter;
    private CityEntry mCity;
    private SwipeRefreshLayout mSwipeContainer;
    private int mPosition = 0;

    public static WeatherFragment newInstance(CityEntry city) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_CITY, city);
        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);
        Log.d(TAG, "newInstance: " + fragment);
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

        mCity = getArguments().getParcelable(ARG_CITY);
        initViewModel();

        initUI(view);
        restoreState(savedInstanceState);
    }

    private void initUI(@NonNull View view) {
        initToolbar();
        mEmptyListMessage = view.findViewById(R.id.tv_empty_list_message);
        mWeatherRecyclerView = view.findViewById(R.id.recycler_view_forecast);
        mSwipeContainer = view.findViewById(R.id.swipeContainer);
        mSwipeContainer.setOnRefreshListener(this::refreshWeather);
        initRecyclerView();
    }

    private void initToolbar() {
        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null ) {
            supportActionBar.setTitle(mCity.name);
        }
    }

    private void initRecyclerView() {
        mWeatherAdapter = new WeatherAdapter(getContext());

        mWeatherRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mWeatherRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mWeatherRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mWeatherRecyclerView.setAdapter(mWeatherAdapter);
    }

    private void initViewModel() {
        // Get the ViewModel from the factory
        WeatherViewModelFactory factory = Injector.INSTANCE.provideWeatherViewModelFactory(getContext().getApplicationContext(), mCity);
        mViewModel = ViewModelProviders.of(this, factory).get(WeatherViewModel.class);
        mViewModel.getForecast().observe(this, this::onWeatherUpdated);
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt(KEY_FIST_VISIBLE_POSITION, RecyclerView.NO_POSITION);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        int firstVisiblePosition = ((LinearLayoutManager) mWeatherRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        outState.putInt(KEY_FIST_VISIBLE_POSITION, firstVisiblePosition);
    }

    private void refreshWeather() {
        mPosition = 0;
        mViewModel.refreshWeather();
    }

    private void onWeatherUpdated(List<WeatherEntry> forecast) {
        mSwipeContainer.setRefreshing(false);
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

    private void displayWeather(@NonNull List<WeatherEntry> weather) {
        mEmptyListMessage.setVisibility(View.INVISIBLE);
        mWeatherRecyclerView.setVisibility(View.VISIBLE);

        mWeatherAdapter.setForecast(weather);

        tryToRestoreScrolledPosition(weather.size());
    }


    private void tryToRestoreScrolledPosition(int itemsCount) {
        if (mPosition != RecyclerView.NO_POSITION && mPosition < itemsCount) {
            mWeatherRecyclerView.smoothScrollToPosition(mPosition);
            mPosition = RecyclerView.NO_POSITION;
        }
    }
}
