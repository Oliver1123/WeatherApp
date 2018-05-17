package com.oliver.weatherapp.domain.model

data class City(
        val id: Long = 0,
        val name: String? = null,
        val address: String? = null,
        val latitude: Double = 0.toDouble(),
        val longitude: Double = 0.toDouble()
) {
    constructor(name: String?, address: String?, latitude: Double, longitude: Double) :
            this(0, name, address, latitude, longitude)
}