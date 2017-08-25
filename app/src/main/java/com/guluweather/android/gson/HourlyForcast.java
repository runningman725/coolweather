package com.guluweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 2017/8/20.
 */

public class HourlyForcast {

    @SerializedName("date")
    public String date;

    @SerializedName("hum")
    public String hum;

    @SerializedName("pop")
    public String pop;

    @SerializedName("pres")
    public String press;

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("wind")
    public Wind wind;

    public class Wind {
        @SerializedName("dir")
        public String dir;
    }
}
