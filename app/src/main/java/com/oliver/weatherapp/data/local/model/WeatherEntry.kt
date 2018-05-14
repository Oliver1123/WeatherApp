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
    var min: Double = 0.toDouble(),
    var max: Double = 0.toDouble(),
    var humidity: Double = 0.toDouble(),
    var wind: Double = 0.toDouble(),
    var degrees: Double = 0.toDouble(),
    var rainVolume: Double = 0.toDouble()
) {

    fun areTheSame(entry: WeatherEntry): Boolean {
        return cityID == entry.cityID && date == entry.date
    }
}
