package com.oliver.weatherapp.screens.forecast;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oliver.weatherapp.R;
import com.oliver.weatherapp.data.local.model.WeatherEntry;

import java.util.ArrayList;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private final List<WeatherEntry> mWeatherEntries = new ArrayList<>();
    private final LayoutInflater mInflater;

    public WeatherAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setForecast(@NonNull List<WeatherEntry> weatherEntries) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                new WeatherDiffCallback(mWeatherEntries, weatherEntries));
        diffResult.dispatchUpdatesTo(this);

        mWeatherEntries.clear();
        mWeatherEntries.addAll(weatherEntries);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mWeatherEntries.get(position));
    }

    @Override
    public int getItemCount() {
        return mWeatherEntries.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mDate;


        public ViewHolder(View itemView) {
            super(itemView);
            mDate = itemView.findViewById(R.id.date);
        }

        void bind(WeatherEntry WeatherEntry) {
            mDate.setText(WeatherEntry.date.toString());
        }
    }

}
