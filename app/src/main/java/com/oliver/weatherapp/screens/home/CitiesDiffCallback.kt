package com.oliver.weatherapp.screens.home

import android.support.v7.util.DiffUtil

import com.oliver.weatherapp.domain.model.City

class CitiesDiffCallback(
        private val oldData: List<City>,
        private val newData: List<City>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].id == newData[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition] == newData[newItemPosition]
    }
}