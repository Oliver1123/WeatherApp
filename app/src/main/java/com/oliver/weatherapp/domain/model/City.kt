package com.oliver.weatherapp.domain.model

data class City(
        var id: Long = 0,
        var name: String? = null,
        var address: String? = null,
        var latitude: Double = 0.toDouble(),
        var longitude: Double = 0.toDouble()
) {
    constructor(name: String?, address: String?, latitude: Double, longitude: Double) :
            this(0, name, address, latitude, longitude)
}