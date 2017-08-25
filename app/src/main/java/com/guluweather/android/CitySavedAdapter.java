package com.guluweather.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guluweather.android.db.CitySaved;

import java.util.List;

/**
 * Created by Admin on 2017/8/13.
 */

public class CitySavedAdapter extends BaseAdapter{

    private List<CitySaved> mCitySaved;
    private LayoutInflater mInflator;
    private Bitmap mBitmap;

    public CitySavedAdapter(Context context, List<CitySaved> citySavedList, Bitmap bitmap) {
        mCitySaved=citySavedList;
        mInflator = LayoutInflater.from(context);
        mBitmap=bitmap;
    }

    @Override
    public int getCount() {
        return mCitySaved.size();
    }

    @Override
    public Object getItem(int position) {
        return mCitySaved.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView = mInflator.inflate(R.layout.activity_city_saved_item, parent, false);
            viewHolder.tvCityTitle = (TextView) convertView.findViewById(R.id.title_city);
            viewHolder.tvWeatherDegree = (TextView) convertView.findViewById(R.id.degree_text);
            viewHolder.tvWeatherInfo = (TextView) convertView.findViewById(R.id.weather_info_text);
            viewHolder.ivCitySaved = (ImageView) convertView.findViewById(R.id.city_saved_image);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tvCityTitle.setText(mCitySaved.get(position).titleCity);
        viewHolder.tvWeatherDegree.setText(mCitySaved.get(position).weatherDegree);
        viewHolder.tvWeatherInfo.setText(mCitySaved.get(position).weatherInfo);
        viewHolder.ivCitySaved.setImageBitmap(mBitmap);
        return convertView;
    }

    class ViewHolder{
        public TextView tvCityTitle,tvWeatherDegree,tvWeatherInfo;
        public ImageView ivCitySaved;
    }
}
