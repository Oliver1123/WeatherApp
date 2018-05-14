package com.oliver.weatherapp.data.local.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

import com.oliver.weatherapp.data.local.model.CityEntry

@Dao
interface CitiesDao {

    @Query("SELECT * FROM cities ORDER BY id DESC")
    fun getAll(): LiveData<List<CityEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(city: CityEntry)

    @Query("DELETE FROM cities WHERE id = :id")
    fun deleteCity(id: Long)

}
