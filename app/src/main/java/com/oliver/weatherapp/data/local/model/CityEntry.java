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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CityEntry)) return false;

        CityEntry cityEntry = (CityEntry) o;

        if (id != cityEntry.id) return false;
        if (Double.compare(cityEntry.latitude, latitude) != 0) return false;
        if (Double.compare(cityEntry.longitude, longitude) != 0) return false;
        if (name != null ? !name.equals(cityEntry.name) : cityEntry.name != null) return false;
        return address != null ? address.equals(cityEntry.address) : cityEntry.address == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
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
