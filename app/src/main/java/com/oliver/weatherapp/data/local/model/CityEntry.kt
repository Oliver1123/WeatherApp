package com.oliver.weatherapp.data.local.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "cities")
data class CityEntry(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var name: String? = null,
        var address: String? = null,
        var latitude: Double = 0.toDouble(),
        var longitude: Double = 0.toDouble()
) : Parcelable