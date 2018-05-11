package com.oliver.weatherapp.screens.forecast;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oliver.weatherapp.R;
import com.oliver.weatherapp.data.local.model.WeatherEntry;
import com.oliver.weatherapp.utils.DateUtils;
import com.oliver.weatherapp.utils.WeatherUtils;

import java.util.ArrayList;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private final List<WeatherEntry> mWeatherEntries = new ArrayList<>();
    private final LayoutInflater mInflater;

    private final int TYPE_NOW = 0;
    private final int TYPE_WEATHER_ITEM = 1;

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
        if (viewType == TYPE_NOW) {
            View view = mInflater.inflate(R.layout.item_weather_now, parent, false);
            return new NowViewHolder(view);
        } else {
            View view = mInflater.inflate(R.layout.item_weather, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mWeatherEntries.get(position));
    }

    @Override
    public int getItemCount() {
        return mWeatherEntries.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_NOW;
        } else {
            return TYPE_WEATHER_ITEM;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        protected final ImageView weatherIcon;
        protected final TextView dateView;
        protected final TextView descriptionView;
        protected final TextView maxTemperatureView;
        protected final TextView minTemperatureView;

        protected final Context mContext;


        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            weatherIcon = itemView.findViewById(R.id.weather_icon);
            dateView = itemView.findViewById(R.id.date);
            descriptionView = itemView.findViewById(R.id.weather_description);
            maxTemperatureView = itemView.findViewById(R.id.high_temperature);
            minTemperatureView = itemView.findViewById(R.id.low_temperature);
        }

        void bind(WeatherEntry weatherEntry) {
            int weatherIconId = weatherEntry.weatherIconId;
            int weatherImageResourceId = WeatherUtils.getResourceIdForWeatherCondition(weatherIconId);
            weatherIcon.setImageResource(weatherImageResourceId);

            long dateInMillis = weatherEntry.date.getTime();
            String dateString = DateUtils.getFriendlyDateString(dateInMillis);
            dateView.setText(dateString);

            String description = WeatherUtils.getStringForWeatherCondition(mContext, weatherIconId);
            String descriptionA11y = mContext.getString(R.string.a11y_forecast, description);
            this.descriptionView.setText(description);
            this.descriptionView.setContentDescription(descriptionA11y);

            double highInCelsius = weatherEntry.max;
            String highString = WeatherUtils.formatTemperature(mContext, highInCelsius);
            String highA11y = mContext.getString(R.string.a11y_high_temp, highString);
            maxTemperatureView.setText(highString);
            maxTemperatureView.setContentDescription(highA11y);

            double lowInCelsius = weatherEntry.min;
            String lowString = WeatherUtils.formatTemperature(mContext, lowInCelsius);
            String lowA11y = mContext.getString(R.string.a11y_low_temp, lowString);
            minTemperatureView.setText(lowString);
            maxTemperatureView.setContentDescription(lowA11y);
        }
    }

    class NowViewHolder extends ViewHolder {

        private final TextView humidityView;
        private final TextView windView;
        private final TextView rainVolumeView;

        public NowViewHolder(View itemView) {
            super(itemView);
            humidityView = itemView.findViewById(R.id.humidity);
            windView = itemView.findViewById(R.id.wind);
            rainVolumeView = itemView.findViewById(R.id.rainVolume);
        }

        @Override
        void bind(WeatherEntry weatherEntry) {
            super.bind(weatherEntry);

            dateView.setText(mContext.getString(R.string.now));

            double humidity = weatherEntry.humidity;
            String humidityString = mContext.getString(R.string.format_humidity, humidity);
            String humidityA11y = mContext.getString(R.string.a11y_humidity, humidityString);
            humidityView.setText(humidityA11y);
            humidityView.setContentDescription(humidityA11y);


            double windSpeed = weatherEntry.wind;
            double windDirection = weatherEntry.degrees;
            String windString = WeatherUtils.getFormattedWind(mContext, windSpeed, windDirection);
            String windA11y = mContext.getString(R.string.a11y_wind, windString);
            windView.setText(windA11y);
            windView.setContentDescription(windA11y);

            double rain = weatherEntry.rainVolume;
            String rainString = mContext.getString(R.string.format_rain, rain);
            String rainA11y = mContext.getString(R.string.a11y_rain, rainString);
            rainVolumeView.setText(rainA11y);
            rainVolumeView.setContentDescription(humidityA11y);
        }
    }

}
