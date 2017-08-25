package com.guluweather.android.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Admin on 2017/8/1.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);//enqueue方法已经开启了子线程
    }
}
