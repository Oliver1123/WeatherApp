package com.oliver.weatherapp.screens.forecast

import android.support.v7.util.DiffUtil

import com.oliver.weatherapp.domain.model.WeatherItem

class WeatherDiffCallback(
        private val oldData: List<WeatherItem>,
        private val newData: List<WeatherItem>
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