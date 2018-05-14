package com.oliver.weatherapp.screens.forecast

import android.support.v7.util.DiffUtil

import com.oliver.weatherapp.data.local.model.WeatherEntry

class WeatherDiffCallback(
        private val oldData: List<WeatherEntry>,
        private val newData: List<WeatherEntry>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].areTheSame(newData[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition] == newData[newItemPosition]
    }
}