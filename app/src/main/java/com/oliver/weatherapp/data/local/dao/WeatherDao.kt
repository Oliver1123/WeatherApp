package com.oliver.weatherapp.data.local.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.oliver.weatherapp.data.local.model.WeatherEntry
import java.util.*

@Dao
interface WeatherDao {

    @Query("SELECT * FROM forecast WHERE cityID = :cityID")
    fun getForecast(cityID: Long): LiveData<List<WeatherEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun bulkInsert(vararg weatherEntries: WeatherEntry)

    @Query("DELETE FROM forecast WHERE date < :date")
    fun deleteOldWeather(date: Date)

    @Query("SELECT COUNT(*) FROM forecast WHERE date >= :date AND cityID = :cityID")
    fun countFutureWeatherForCity(cityID: Long, date: Date): Int

    @Query("DELETE FROM forecast WHERE cityID = :cityID")
    fun deleteWeatherForCity(cityID: Long): Int
}
