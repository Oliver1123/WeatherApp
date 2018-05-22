package com.oliver.weatherapp.data.local.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntry(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var name: String? = null,
        var address: String? = null,
        var latitude: Double = 0.0,
        var longitude: Double = 0.0
) {
    @Ignore
    constructor(name: String?, address: String?, latitude: Double, longitude: Double) :
            this(id = 0, name = name, address = address, latitude = latitude, longitude = longitude)
}