package com.guluweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.guluweather.android.gson.Forecast;
import com.guluweather.android.gson.HourlyForcast;
import com.guluweather.android.gson.Weather;
import com.guluweather.android.service.AutoUpdateService;
import com.guluweather.android.util.HttpUtil;
import com.guluweather.android.util.Utility;
import com.mob.MobSDK;
import com.mob.commons.SHARESDK;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bingPicImg;
    public SwipeRefreshLayout swipeRefresh;
    private String weatherId;
    public DrawerLayout drawerLayout;
    private Button navButton;
    private Button shareButton;
    private String weatherString;
    private Button savedCityList;
    private TextView tvDate;
    private TextView tvHumidity;
    private TextView tvRain;
    private TextView tvPressure;
    private TextView tvTemperature;
    private TextView tvWindDirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MobSDK.init(WeatherActivity.this, "200cd0fef36f0", "910de309787467a67bf8d518beb3b864");
        setContentView(com.guluweather.android.R.layout.activity_weather);
//        if(Build.VERSION.SDK_INT>=21){
//            View decorView=getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        weatherLayout = (ScrollView) findViewById(com.guluweather.android.R.id.weather_layout);
        titleCity = (TextView) findViewById(com.guluweather.android.R.id.title_city);
        titleUpdateTime = (TextView) findViewById(com.guluweather.android.R.id.title_update_time);
        degreeText = (TextView) findViewById(com.guluweather.android.R.id.degree_text);
        weatherInfoText = (TextView) findViewById(com.guluweather.android.R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(com.guluweather.android.R.id.forecast_layout);
        aqiText = (TextView) findViewById(com.guluweather.android.R.id.aqi_text);
        pm25Text = (TextView) findViewById(com.guluweather.android.R.id.pm25_text);
        comfortText = (TextView) findViewById(com.guluweather.android.R.id.comfort_text);
        carWashText = (TextView) findViewById(com.guluweather.android.R.id.car_wash_text);
        sportText = (TextView) findViewById(com.guluweather.android.R.id.sport_text);
        bingPicImg = (ImageView) findViewById(com.guluweather.android.R.id.bing_pic_img);
        swipeRefresh = (SwipeRefreshLayout) findViewById(com.guluweather.android.R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(com.guluweather.android.R.color.colorPrimary);
        drawerLayout = (DrawerLayout) findViewById(com.guluweather.android.R.id.drawer_layout);
        navButton = (Button) findViewById(com.guluweather.android.R.id.nav_button);
        shareButton = (Button) findViewById(R.id.ic_share);
        savedCityList = (Button) findViewById(R.id.ic_plus_city);
        tvDate = (TextView) findViewById(com.guluweather.android.R.id.tv_time);
        tvHumidity = (TextView) findViewById(com.guluweather.android.R.id.tv_humidity);
        tvRain = (TextView) findViewById(com.guluweather.android.R.id.tv_rain);
        tvPressure = (TextView) findViewById(com.guluweather.android.R.id.tv_pressure);
        tvTemperature = (TextView) findViewById(com.guluweather.android.R.id.tv_temp);
        tvWindDirect = (TextView) findViewById(com.guluweather.android.R.id.tv_wind_direct);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        weatherString=prefs.getString("weather", null);
        final String bingPic = prefs.getString("bing_pic", null);
        if(weatherString!=null){
            final Weather weather=Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            Log.d("WeatherActivity", "wtf");
            showWeatherInfo(weather);
            savedCityList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    byte[] imgBytes = bingPic.getBytes();
                    Intent intent = new Intent(WeatherActivity.this, CitySavedActivity.class);
//                    intent.putExtra("city_name", weather.basic.cityname);
//                    intent.putExtra("weather_degree", weather.now.temperature + "℃");
//                    intent.putExtra("weather_info", weather.now.more.info);
                    intent.putExtra("bing_pic", imgBytes);
                    intent.putExtra("weather", weatherString);
                    startActivity(intent);
                }
            });

        }else{
            weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }

        if(bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

    }

    private void showShare() {
            OnekeyShare oks = new OnekeyShare();
            //关闭sso授权
            oks.disableSSOWhenAuthorize();
            // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
            //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
            oks.setTitle("标题");
            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
            oks.setTitleUrl("http://sharesdk.cn");
            // text是分享文本，所有平台都需要这个字段
            oks.setText("我是分享文本");
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl("http://sharesdk.cn");//"https://free-api.heweather.com/v5/weather?city=" + weatherId +
//        "&key=0f933eed22b64039b230901b8a60dfa1"
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
            oks.setComment("我是测试评论文本");
            // site是分享此内容的网站名称，仅在QQ空间使用
            oks.setSite(getString(R.string.app_name));
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSiteUrl("http://sharesdk.cn");

            // 启动分享GUI
            oks.show(this);
    }

    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(WeatherActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    public void requestWeather(final String weatherId) {
        final String weatherUrl = "https://free-api.heweather.com/v5/weather?city=" + weatherId +
                "&key=0f933eed22b64039b230901b8a60dfa1";
        Log.d("TAG", "requestWeatherUrl"+weatherUrl);
                HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseText = response.body().string();
                        final Weather weather=Utility.handleWeatherResponse(responseText);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(weather!=null && "ok".equals(weather.status)){
                                    SharedPreferences.Editor editor = PreferenceManager.
                                            getDefaultSharedPreferences(WeatherActivity.this).edit();
                                    editor.putString("weather", responseText);
//                                    editor.remove("weather");
                                    editor.apply();
                                    showWeatherInfo(weather);
                                    loadBingPic();

                                }else {
                                    Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                                }
                                swipeRefresh.setRefreshing(false);
                            }
                        });

                    }
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                                swipeRefresh.setRefreshing(false);
                            }
                        });
                    }
                });
            }

    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityname;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree=weather.now.temperature+"℃";
        String weatherInfo=weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for(Forecast forecast:weather.forecastList){
            Log.d("TAG", "forecastList ");
            View view=LayoutInflater.from(this).inflate(com.guluweather.android.R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(com.guluweather.android.R.id.date_text);
            TextView infoText = (TextView) view.findViewById(com.guluweather.android.R.id.info_text);
            TextView maxText = (TextView) view.findViewById(com.guluweather.android.R.id.max_text);
            TextView minText = (TextView) view.findViewById(com.guluweather.android.R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if(weather.aqi!=null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }

        for(HourlyForcast hourlyForcast:weather.hourlyForcastList){
            String humid=hourlyForcast.hum+"%";
            String rain=hourlyForcast.pop+"%";
            String press=hourlyForcast.press+"hPa";
            String temp=hourlyForcast.temperature+"℃";
            tvDate.setText(hourlyForcast.date.split(" ")[1]);
            tvHumidity.setText(humid);
            tvRain.setText(rain);
            tvPressure.setText(press);
            tvTemperature.setText(temp);
            tvWindDirect.setText(hourlyForcast.wind.dir);
        }

        String comfort = "舒适度:" + weather.suggestion.comfort.info;
        String carWash = "洗车指数:" + weather.suggestion.carWash.info;
        String sport = "运动建议:" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
        if(weather!=null && "ok".equals(weather.status)){
            Intent intent = new Intent(this, AutoUpdateService.class);
            startService(intent);
        }else {
            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
        }
    }

}











