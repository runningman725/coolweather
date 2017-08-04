package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 2017/8/2.
 */

public class Basic {

    @SerializedName("city")
    public String cityname;

    @SerializedName("id")
    public String weatherId;

    @SerializedName("update")
    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
