package com.oliver.weatherapp.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.oliver.weatherapp.data.local.dao.CitiesDao;
import com.oliver.weatherapp.data.local.model.CityEntry;

@Database(entities = {CityEntry.class}, version = 1)
public abstract class WeatherDatabase extends RoomDatabase {

    private static final String LOG_TAG = WeatherDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "weather";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static WeatherDatabase sInstance;

    public static WeatherDatabase getInstance(Context context) {
        Log.d(LOG_TAG, "Getting the database");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room
                        .databaseBuilder(context.getApplicationContext(), WeatherDatabase.class, DATABASE_NAME)
                        .build();
                Log.d(LOG_TAG, "Made new database");
            }
        }
        return sInstance;
    }

    // The associated DAOs for the database
    public abstract CitiesDao citiesDao();
}

