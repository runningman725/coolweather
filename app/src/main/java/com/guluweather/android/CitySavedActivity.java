package com.guluweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.guluweather.android.db.City;
import com.guluweather.android.db.CitySaved;
import com.guluweather.android.db.County;
import com.guluweather.android.db.Province;
import com.guluweather.android.gson.Weather;
import com.guluweather.android.util.HttpUtil;
import com.guluweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CitySavedActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TextView tvCityName;
    private TextView tvWeatherDegree;
    private TextView tvWeatherInfo;
    private ImageView ivCitySaved;
    private Bitmap bitmap;
    private ListView citySavedListView;
    private String cn;
    private String wd;
    private String wi;
    private Button backButton;
    private List<CitySaved> mCitySaved = new ArrayList<>();
    private CitySavedAdapter adapter;
    private Weather weather;
    private List<Weather> weatherList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_saved);
        citySavedListView = (ListView) findViewById(R.id.city_saved_list);
        backButton = (Button) findViewById(R.id.back_button);
        Intent intent=getIntent();
        String weatherString= (String) intent.getSerializableExtra("weather");
        weather = Utility.handleWeatherResponse(weatherString);
        cn = weather.basic.cityname;
        wd = weather.now.temperature+"℃";
        wi = weather.now.more.info;
        weatherList.add(weather);
        String weatherId = intent.getStringExtra("weather_id");
        if(weatherId!=null){
            requestWeather(weatherId);
        }
//        cn = intent.getStringExtra("city_name");
//        wd = intent.getStringExtra("weather_degree");
//        wi = intent.getStringExtra("weather_info");

//        showCityWeather();
        //需要往citySavedList中存入数据
        if(weather!=null){
            for(int i=0;i<weatherList.size();i++){
                weather = weatherList.get(i);
                CitySaved citySaved=new CitySaved();
                citySaved.setTitleCity(weather.basic.cityname);
                citySaved.setWeatherDegree(weather.now.temperature+"℃");
                citySaved.setWeatherInfo(weather.now.more.info);
//        citySaved.save();
                mCitySaved.add(citySaved);
            }
//            adapter.notifyDataSetChanged();
        }

        byte buff[] = intent.getByteArrayExtra("bing_pic");
        bitmap = BitmapFactory.decodeByteArray(buff, 0, buff.length);
        adapter=new CitySavedAdapter(CitySavedActivity.this,mCitySaved,bitmap);
        citySavedListView.setAdapter(adapter);
        citySavedListView.setOnItemClickListener(this);
        if(Build.VERSION.SDK_INT>=21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(CitySavedActivity.this, WeatherActivity.class);
            startActivity(intent);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view=getLayoutInflater().inflate(R.layout.activity_main, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(CitySavedActivity.this);
                builder.setView(view);
                builder.setCancelable(true);
                builder.show();
//                ListView countyListView = (ListView) view.findViewById(R.id.list_view);
//                countyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                        final ChooseAreaFragment caf=new ChooseAreaFragment();
//                        caf.getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                List<Province> provinceList = DataSupport.findAll(Province.class);
//                                Province selectedProvince = provinceList.get(position);
//                                List<City> cityList = DataSupport.where("provinceid=?",String.valueOf(selectedProvince.getId())).find(City.class);
//                                City selectedCity = cityList.get(position);
//                                List<County> countyList = DataSupport.where("cityid=?", String.valueOf(selectedCity.getId())).find(County.class);
//                                String weatherId=countyList.get(position).getWeatherId();
//                                if(ChooseAreaFragment.currentLevel == ChooseAreaFragment.LEVEL_COUNTY){
//                                    Intent intent = new Intent(caf.getActivity(),CitySavedActivity.class);
//                                    intent.putExtra("weather_id", weatherId);
//                                    startActivity(intent);
//                                    caf.getActivity().finish();
//                                }
//                            }
//                        });
//
//                    }
//                });
            }
        });

}

    private void requestWeather(final String weatherId) {
        final String weatherUrl = "https://free-api.heweather.com/v5/weather?city=" + weatherId +
                "&key=0f933eed22b64039b230901b8a60dfa1";
        Log.d("TAG", "requestWeatherUrl"+weatherUrl);
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather1=Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather1!=null && "ok".equals(weather1.status)){
                            CitySaved citySaved=new CitySaved();
                            citySaved.setTitleCity(weather.basic.cityname);
                            citySaved.setWeatherDegree(weather.now.temperature+"℃");
                            citySaved.setWeatherInfo(weather.now.more.info);
//        citySaved.save();
                            mCitySaved.add(citySaved);
                            weatherList.add(weather1);
                            adapter.notifyDataSetChanged();

                        }else {
                            Toast.makeText(CitySavedActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CitySavedActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(CitySavedActivity.this, WeatherActivity.class);
        startActivity(intent);
    }
}
