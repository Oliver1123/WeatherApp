package com.oliver.weatherapp.screens.forecast;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.oliver.weatherapp.data.local.model.WeatherEntry;

import java.util.List;

public class WeatherDiffCallback extends DiffUtil.Callback {

    private List<WeatherEntry> oldData;
    private List<WeatherEntry> newData;

    public WeatherDiffCallback(@NonNull List<WeatherEntry> oldData, @NonNull List<WeatherEntry> newData) {
        this.oldData = oldData;
        this.newData = newData;
    }

    @Override
    public int getOldListSize() {
        return oldData.size();
    }

    @Override
    public int getNewListSize() {
        return newData.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldData.get(oldItemPosition).id == newData.get(newItemPosition).id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldData.get(oldItemPosition).equals(newData.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}