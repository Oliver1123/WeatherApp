package com.oliver.weatherapp.data.local.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "forecast",
        primaryKeys = {"cityID", "date"},
        foreignKeys = @ForeignKey(entity = CityEntry.class, parentColumns = "id", childColumns = "cityID", onDelete = CASCADE))
public class WeatherEntry {

    public long cityID;
    public int weatherIconId;
    @NonNull
    public Date date = new Date(0);
    public double min;
    public double max;
    public double humidity;
    public double wind;
    public double degrees;
    public double rainVolume;

    public boolean areTheSame(WeatherEntry entry) {
        return cityID == entry.cityID &&
                date.equals(entry.date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeatherEntry)) return false;

        WeatherEntry that = (WeatherEntry) o;

        if (cityID != that.cityID) return false;
        if (weatherIconId != that.weatherIconId) return false;
        if (Double.compare(that.min, min) != 0) return false;
        if (Double.compare(that.max, max) != 0) return false;
        if (Double.compare(that.humidity, humidity) != 0) return false;
        if (Double.compare(that.wind, wind) != 0) return false;
        if (Double.compare(that.degrees, degrees) != 0) return false;
        if (Double.compare(that.rainVolume, rainVolume) != 0) return false;
        return date.equals(that.date);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (cityID ^ (cityID >>> 32));
        result = 31 * result + weatherIconId;
        result = 31 * result + date.hashCode();
        temp = Double.doubleToLongBits(min);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(max);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(humidity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(wind);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(degrees);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(rainVolume);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "WeatherEntry{" +
                "cityId=" + cityID +
                ", weatherIconId=" + weatherIconId +
                ", date=" + date +
                ", min=" + min +
                ", max=" + max +
                ", humidity=" + humidity +
                ", wind=" + wind +
                ", degrees=" + degrees +
                ", rainVolume=" + rainVolume +
                '}';
    }
}
