package com.guluweather.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import cn.sharesdk.onekeyshare.OnekeyShare;


public class MainActivity extends AppCompatActivity {

        private IntentFilter intentFilter;
        private NetworkChangeReceiver networkChangeReceiver;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(com.guluweather.android.R.layout.activity_main);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            if(prefs.getString("weather",null)!=null){
//            SharedPreferences.Editor editor = prefs.edit();
//                editor.remove("weather");
//                editor.apply();
//                finish();
                Intent intent = new Intent(this, WeatherActivity.class);
                startActivity(intent);
                finish();
            }
            intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            networkChangeReceiver=new NetworkChangeReceiver();
            registerReceiver(networkChangeReceiver, intentFilter);
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    class NetworkChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
//            if(networkInfo!=null && networkInfo.isAvailable()){
//                Toast.makeText(context,"Network is available",Toast.LENGTH_SHORT).show();
//            }else{
//                Toast.makeText(context,"Network is unavailable",Toast.LENGTH_SHORT).show();
//            }
            NetworkInfo gprs = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (gprs.isConnected()){
                Toast.makeText(context, "数据流量连接", Toast.LENGTH_SHORT).show();
            }else if(wifi.isConnected()){
                Toast.makeText(context,"wifi连接",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
