package com.guluweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/8/2.
 */

public class Weather implements Serializable{
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
    @SerializedName("hourly_forecast")
    public List<HourlyForcast> hourlyForcastList;

}
