package com.oliver.weatherapp.data.local.model;

import java.util.Date;

public class WeatherEntry {

    public int id;
    public int weatherIconId;
    public Date date;
    public double min;
    public double max;
    public double humidity;
    public double wind;
    public double degrees;
    public double rainVolume;

    @Override
    public String toString() {
        return "WeatherEntry{" +
                "id=" + id +
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
