package com.oliver.weatherapp.data.local.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "cities")
public class CityEntry {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public String address;
    public double latitude;
    public double longitude;

    public CityEntry(String name, String address, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "CityEntry{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
