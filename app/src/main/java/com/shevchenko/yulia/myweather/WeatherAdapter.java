package com.shevchenko.yulia.myweather;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shevchenko.yulia.myweather.async.DownloadImageTask;
import com.shevchenko.yulia.myweather.helper.entities.Weather;

import java.util.ArrayList;

public class WeatherAdapter extends BaseAdapter {

    private Context ctx;
    private LayoutInflater lInflater;

    public WeatherAdapter(Context ctx, LayoutInflater lInflater, ArrayList<Weather> weathers) {
        this.ctx = ctx;
        this.lInflater = lInflater;
        this.weathers = weathers;
    }

    private ArrayList<Weather> weathers;
    private Activity activity;

    @Override
    public int getCount() {
        return weathers.size();
    }

    @Override
    public Object getItem(int i) {
        return weathers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public WeatherAdapter(Context ctx, Activity activity, ArrayList<Weather> weathers) {
        this.ctx = ctx;
        this.weathers = weathers;
        this.activity = activity;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.part, viewGroup, false);
        }
        Weather weather = (Weather) getItem(i);
        ((TextView) view.findViewById(R.id.date_time)).setText(weather.getDate());
        ((TextView) view.findViewById(R.id.temperature)).setText(String.valueOf(weather.getTemperature() + " °C"));
        ((TextView) view.findViewById(R.id.tvPressure)).setText(String.format("%s мм.рт.ст.", String.valueOf(weather.getPressure())));
        ((TextView) view.findViewById(R.id.tvHumidity)).setText(String.format("%s %%", String.valueOf(weather.getHumidity())));
        String[] words = weather.getWind().split(", ");
        ((TextView) view.findViewById(R.id.tvWind)).setText(words[0]);
        if (words.length == 1) {
            ((TextView) view.findViewById(R.id.tvWindDeg)).setText("0 м/с");
        } else {
            ((TextView) view.findViewById(R.id.tvWindDeg)).setText(words[1]);
        }
        new DownloadImageTask((ImageView) view.findViewById(R.id.weather_icon))
                .execute("http://" + weather.getUrl());
        return view;
    }
}
