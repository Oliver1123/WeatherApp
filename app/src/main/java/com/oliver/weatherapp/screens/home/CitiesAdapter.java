package com.oliver.weatherapp.screens.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oliver.weatherapp.R;
import com.oliver.weatherapp.data.local.model.CityEntry;

import java.util.ArrayList;
import java.util.List;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> {

    private final List<CityEntry> mCities = new ArrayList<>();
    private final LayoutInflater mInflater;

    public CitiesAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setCities(@NonNull List<CityEntry> cities) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                new CitiesDiffCallback(mCities, cities));
        diffResult.dispatchUpdatesTo(this);

        mCities.clear();
        mCities.addAll(cities);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mCities.get(position));
    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = ViewHolder.class.getSimpleName();
        private final TextView mName;
        private final TextView mAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_city_name);
            mAddress = itemView.findViewById(R.id.tv_city_address);
            itemView.findViewById(R.id.ic_delete_city).setOnClickListener(this::onDeleteClick);
        }

        private void onDeleteClick(View view) {
            Log.d(TAG, "onDeleteClick for: " + getAdapterPosition());
        }

        void bind(CityEntry cityEntry) {
            mName.setText(cityEntry.name);
            mAddress.setText(cityEntry.address);
        }
    }
}
