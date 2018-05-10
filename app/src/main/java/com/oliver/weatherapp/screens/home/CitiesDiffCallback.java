package com.oliver.weatherapp.screens.home;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.oliver.weatherapp.data.local.model.CityEntry;

import java.util.List;

public class CitiesDiffCallback extends DiffUtil.Callback {

    private List<CityEntry> oldData;
    private List<CityEntry> newData;

    public CitiesDiffCallback(@NonNull List<CityEntry> oldData, @NonNull List<CityEntry> newData) {
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