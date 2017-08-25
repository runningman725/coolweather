package com.guluweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Admin on 2017/8/14.
 */

public class CitySaved extends DataSupport{
    public String titleCity;
    public String weatherDegree;
    public String weatherInfo;
    public String imgCitySaved;

    public String getTitleCity() {
        return titleCity;
    }

    public void setTitleCity(String titleCity) {
        this.titleCity = titleCity;
    }

    public String getWeatherDegree() {
        return weatherDegree;
    }

    public void setWeatherDegree(String weatherDegree) {
        this.weatherDegree = weatherDegree;
    }

    public String getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(String weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    public String getImgCitySaved() {
        return imgCitySaved;
    }

    public void setImgCitySaved(String imgCitySaved) {
        this.imgCitySaved = imgCitySaved;
    }
}
