package com.oliver.weatherapp.screens.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
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
    private final OnCityClickListener mCallback;

    public CitiesAdapter(Context context, OnCityClickListener callback) {
        mInflater = LayoutInflater.from(context);
        mCallback = callback;
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

    private void onDeleteClick(ViewHolder holder) {
        int position = holder.getAdapterPosition();
        mCallback.onDeleteClick(holder.itemView, mCities.get(position), position);
    }

    private void onItemClick(ViewHolder holder) {
        int position = holder.getAdapterPosition();
        mCallback.onItemClick(holder.itemView, mCities.get(position), position);
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mName;
        private final TextView mAddress;
        private final View mDeleteIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_city_name);
            mAddress = itemView.findViewById(R.id.tv_city_address);
            mDeleteIcon = itemView.findViewById(R.id.ic_delete_city);

            mDeleteIcon.setOnClickListener(this::onDeleteClick);
            itemView.setOnClickListener(this::onItemClick);
        }

        private void onDeleteClick(View view) {
            CitiesAdapter.this.onDeleteClick(this);
        }

        private void onItemClick(View view) {
            CitiesAdapter.this.onItemClick(this);
        }

        void bind(CityEntry cityEntry) {
            mName.setText(cityEntry.name);
            mName.setContentDescription(
                    mName.getContext().getString(R.string.msg_city_name_content_description, cityEntry.name));

            mAddress.setText(cityEntry.address);
            mAddress.setContentDescription(
                    mAddress.getContext().getString(R.string.msg_city_address_content_description, cityEntry.address));

            mDeleteIcon.setContentDescription(
                    mDeleteIcon.getContext().getString(R.string.msg_city_delete_button_content_description, cityEntry.name));
        }
    }

    public interface OnCityClickListener {
        void onDeleteClick(View view, CityEntry city, int position);
        void onItemClick(View view, CityEntry city, int position);
    }

}
