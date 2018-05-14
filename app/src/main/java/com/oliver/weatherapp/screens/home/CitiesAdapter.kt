package com.oliver.weatherapp.screens.home

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.oliver.weatherapp.R
import com.oliver.weatherapp.data.local.model.CityEntry
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_city.*
import java.util.*

class CitiesAdapter(
        private val context: Context,
        private val callback: OnCityClickListener
) : RecyclerView.Adapter<CitiesAdapter.ViewHolder>() {

    private val cities = ArrayList<CityEntry>()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    fun setCities(cities: List<CityEntry>) {
        val diffResult = DiffUtil.calculateDiff(CitiesDiffCallback(this.cities, cities))
        diffResult.dispatchUpdatesTo(this)

        this.cities.clear()
        this.cities.addAll(cities)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_city, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cities[position])
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    private fun onDeleteClick(holder: ViewHolder) {
        val position = holder.adapterPosition
        callback.onDeleteClick(holder.itemView, cities[position], position)
    }

    private fun onItemClick(holder: ViewHolder) {
        val position = holder.adapterPosition
        callback.onItemClick(holder.itemView, cities[position], position)
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            ic_delete_city.setOnClickListener { this@CitiesAdapter.onDeleteClick(this) }
            itemView.setOnClickListener{ this@CitiesAdapter.onItemClick(this) }
        }


        fun bind(cityEntry: CityEntry) {
            tv_city_name.text = cityEntry.name
            tv_city_name.contentDescription = context.getString(R.string.msg_city_name_content_description, cityEntry.name)

            tv_city_address.text = cityEntry.address
            tv_city_address.contentDescription = context.getString(R.string.msg_city_address_content_description, cityEntry.address)

            ic_delete_city.contentDescription = context
                    .getString(R.string.msg_city_delete_button_content_description, cityEntry.name)
        }
    }

    interface OnCityClickListener {
        fun onDeleteClick(view: View, city: CityEntry, position: Int)
        fun onItemClick(view: View, city: CityEntry, position: Int)
    }

}
