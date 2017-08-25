package com.guluweather.android;

import android.app.Application;
import android.content.Context;

import com.mob.MobApplication;
import com.mob.MobSDK;

import org.litepal.LitePal;

/**
 * Created by Admin on 2017/8/9.
 */

public class MyApplication extends MobApplication {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        LitePal.initialize(context);
        MobSDK.init(this,this.a(),this.b());
    }

    private String b() {
        return null;
    }

    private String a() {
        return null;
    }

    public static Context context(){
        return context;
    }
}
