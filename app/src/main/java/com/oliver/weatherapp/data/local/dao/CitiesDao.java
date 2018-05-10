package com.oliver.weatherapp.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.oliver.weatherapp.data.local.model.CityEntry;

import java.util.List;

@Dao
public interface CitiesDao {

    @Query("SELECT * FROM cities ORDER BY id DESC")
    LiveData<List<CityEntry>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CityEntry city);

    @Query("DELETE FROM cities WHERE id = :id")
    void deleteCity(long id);

}
