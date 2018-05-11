package com.oliver.weatherapp.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("speed")
    public double speed;
    @SerializedName("deg")
    public double deg;

}
