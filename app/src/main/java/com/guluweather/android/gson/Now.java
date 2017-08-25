package com.guluweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 2017/8/2.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String info;
    }

}
