package com.shevchenko.yulia.myweather.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shevchenko.yulia.myweather.R;
import com.shevchenko.yulia.myweather.async.DownloadImageTask;
import com.shevchenko.yulia.myweather.model.entities.Weather;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrentWeatherAdapter extends BaseAdapter {

    private ArrayList<Weather> weathers;
    private Context mContext;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.weather_icon)
    ImageView weatherIcon;
    @BindView(R.id.temperature)
    TextView temperature;
    @BindView(R.id.tvHumidity)
    TextView humidity;
    @BindView(R.id.tvPressure)
    TextView pressure;
    @BindView(R.id.tvWind)
    TextView wind;
    @BindView(R.id.tvWindDeg)
    TextView windDeg;

    LayoutInflater lInflater;

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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = lInflater.inflate(R.layout.part, viewGroup, false);
        }
        ButterKnife.bind(this, view);
        Weather weather = weathers.get(i);
        time.setText(weather.getTime());
        new DownloadImageTask(weatherIcon).execute("http://" + weather.getUrl());
        temperature.setText(weather.getTemperature() + mContext.getResources().getString(R.string.celsium));
        humidity.setText(weather.getHumidity() + mContext.getResources().getString(R.string.percent));
        pressure.setText(weather.getPressure() + " мм.рт.ст.");
        String[] words = weather.getWind().split(", ");
        wind.setText(words[0]);
        if (words.length == 1) {
            windDeg.setText("0 м/с");
        } else {
            windDeg.setText(words[1].substring(0, words[1].length()-5) + "м/с");
        }

        return view;
    }

    public CurrentWeatherAdapter(Context context, ArrayList<Weather> weathers){
        this.weathers = weathers;
        mContext = context;
        lInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

}