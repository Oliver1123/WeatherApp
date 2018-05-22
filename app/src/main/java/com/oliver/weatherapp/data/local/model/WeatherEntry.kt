package com.oliver.weatherapp.data.local.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import java.util.*

@Entity(tableName = "forecast",
        primaryKeys = ["cityID", "date"],
        foreignKeys = [ForeignKey(entity = CityEntry::class, parentColumns = ["id"], childColumns = ["cityID"], onDelete = CASCADE)])
data class WeatherEntry (
    var cityID: Long = 0,
    var weatherIconId: Int = 0,
    var date: Date = Date(0),
    var min: Double = 0.0,
    var max: Double = 0.0,
    var humidity: Double = 0.0,
    var wind: Double = 0.0,
    var degrees: Double = 0.0,
    var rainVolume: Double = 0.0
)
