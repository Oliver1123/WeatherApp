package com.oliver.weatherapp.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.oliver.weatherapp.data.local.model.WeatherEntry;

import java.util.Date;
import java.util.List;

@Dao
public interface WeatherDao {

    @Query("SELECT * FROM forecast WHERE cityID = :cityID")
    LiveData<List<WeatherEntry>> getForecast(long cityID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(WeatherEntry... weatherEntries);

    @Query("DELETE FROM forecast WHERE date < :date")
    void deleteOldWeather(Date date);

    @Query("SELECT COUNT(*) FROM forecast WHERE date >= :date AND cityID = :cityID")
    int countFutureWeatherForCity(long cityID, Date date);
}
