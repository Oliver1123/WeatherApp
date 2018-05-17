package com.oliver.weatherapp.screens.forecast

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.oliver.weatherapp.R
import com.oliver.weatherapp.domain.model.WeatherItem
import com.oliver.weatherapp.utils.DateUtils
import com.oliver.weatherapp.utils.WeatherUtils
import kotlinx.android.extensions.LayoutContainer
import java.util.*

class WeatherAdapter(context: Context) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    private val weatherEntries: MutableList<WeatherItem> = ArrayList()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private val TYPE_NOW = 0
    private val TYPE_WEATHER_ITEM = 1

    fun setForecast(entries: List<WeatherItem>) {
        val diffResult = DiffUtil.calculateDiff(WeatherDiffCallback(this.weatherEntries, entries))
        diffResult.dispatchUpdatesTo(this)

        weatherEntries.clear()
        weatherEntries.addAll(entries)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == TYPE_NOW) {
            val view = inflater.inflate(R.layout.item_weather_now, parent, false)
            NowViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_weather, parent, false)
            ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(weatherEntries[position])
    }

    override fun getItemCount(): Int {
        return weatherEntries.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_NOW
        } else {
            TYPE_WEATHER_ITEM
        }
    }

    open inner class ViewHolder(override val containerView: View) :
            RecyclerView.ViewHolder(containerView), LayoutContainer {

        protected val weatherIcon: ImageView = itemView.findViewById(R.id.weather_icon)
        protected val dateView: TextView = itemView.findViewById(R.id.date)
        protected val descriptionView: TextView = itemView.findViewById(R.id.weather_description)
        protected val maxTemperatureView: TextView = itemView.findViewById(R.id.high_temperature)
        protected val minTemperatureView: TextView = itemView.findViewById(R.id.low_temperature)

        protected val context: Context = itemView.context


        internal open fun bind(item: WeatherItem) {
            val weatherIconId = item.weatherIconId
            val weatherImageResourceId = WeatherUtils.getSmallArtResourceIdForWeatherCondition(weatherIconId)
            weatherIcon.setImageResource(weatherImageResourceId)

            val dateInMillis = item.date.time
            val dateString = DateUtils.getFriendlyDateString(context, dateInMillis)
            dateView.text = dateString

            val description = WeatherUtils.getStringForWeatherCondition(context, weatherIconId)
            val descriptionA11y = context.getString(R.string.a11y_forecast, description)
            this.descriptionView.text = description
            this.descriptionView.contentDescription = descriptionA11y

            val highInCelsius = item.max
            val highString = WeatherUtils.formatTemperature(context, highInCelsius)
            val highA11y = context.getString(R.string.a11y_high_temp, highString)
            maxTemperatureView.text = highString
            maxTemperatureView.contentDescription = highA11y

            val lowInCelsius = item.min
            val lowString = WeatherUtils.formatTemperature(context, lowInCelsius)
            val lowA11y = context.getString(R.string.a11y_low_temp, lowString)
            minTemperatureView.text = lowString
            maxTemperatureView.contentDescription = lowA11y
        }
    }

    internal inner class NowViewHolder(itemView: View) : ViewHolder(itemView) {

        private val humidityView: TextView = itemView.findViewById(R.id.humidity)
        private val windView: TextView = itemView.findViewById(R.id.wind)
        private val rainVolumeView: TextView = itemView.findViewById(R.id.rainVolume)

        override fun bind(item: WeatherItem) {
            super.bind(item)

            dateView.text = context.getString(R.string.now)

            val weatherIconId = item.weatherIconId
            val weatherImageResourceId = WeatherUtils.getLargeArtResourceIdForWeatherCondition(weatherIconId)
            weatherIcon.setImageResource(weatherImageResourceId)

            val humidity = item.humidity
            val humidityString = context.getString(R.string.format_humidity, humidity)
            val humidityA11y = context.getString(R.string.a11y_humidity, humidityString)
            humidityView.text = humidityA11y
            humidityView.contentDescription = humidityA11y


            val windSpeed = item.wind
            val windDirection = item.degrees
            val windString = WeatherUtils.getFormattedWind(context, windSpeed, windDirection)
            val windA11y = context.getString(R.string.a11y_wind, windString)
            windView.text = windA11y
            windView.contentDescription = windA11y

            val rain = item.rainVolume
            val rainString = context.getString(R.string.format_rain, rain)
            val rainA11y = context.getString(R.string.a11y_rain, rainString)
            rainVolumeView.text = rainA11y
            rainVolumeView.contentDescription = humidityA11y
        }
    }

}
