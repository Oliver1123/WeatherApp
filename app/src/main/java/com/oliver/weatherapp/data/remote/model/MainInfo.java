package com.oliver.weatherapp.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class MainInfo {
    @SerializedName("temp_min")
    public double tempMin;
    @SerializedName("temp_max")
    public double tempMax;
    @SerializedName("humidity")
    public long humidity;
}
