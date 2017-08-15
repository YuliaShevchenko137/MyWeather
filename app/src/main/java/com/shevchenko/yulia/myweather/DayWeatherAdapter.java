package com.shevchenko.yulia.myweather;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DayWeatherAdapter extends RecyclerView.Adapter {

    private ArrayList<String> weather;
    private Context mContext;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.todayDate)
        TextView todayDate;
        @BindView(R.id.dayTemperature)
        TextView dayTemperature;
        @BindView(R.id.nightTemperature)
        TextView nightTemperature;
        @BindView(R.id.dayIcon)
        ImageView dayIcon;
        @BindView(R.id.nightIcon)
        ImageView nightIcon;
        @BindView(R.id.dayPrecipitation)
        TextView dayPrecipitation;
        @BindView(R.id.nightPrecipitation)
        TextView nightPrecipitation;
        @BindView(R.id.dayWind)
        TextView dayWind;
        @BindView(R.id.nightWind)
        TextView nightWind;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public DayWeatherAdapter(Context context, ArrayList<String> heroes){
        this.weather = heroes;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_weather, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            String str = weather.get(position);
            MyViewHolder newHolder = (MyViewHolder) holder;
            JSONObject jsonObj = new JSONObject(str);
            String dateTime = jsonObj.getString("Date");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date docDate = format.parse(dateTime);
            docDate = new Date(docDate.getTime() + 1000*60*60*3);
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            newHolder.todayDate.setText(df.format(docDate));
            JSONObject temperature = jsonObj.getJSONObject("Temperature");
            String minTemp = temperature.getJSONObject("Minimum").getString("Value");
            newHolder.nightTemperature.setText(String.format("%s%s", minTemp, newHolder.nightTemperature.getText()));
            String maxTemp = temperature.getJSONObject("Maximum").getString("Value");
            newHolder.dayTemperature.setText(String.format("%s%s", maxTemp, newHolder.dayTemperature.getText()));
            JSONObject day = jsonObj.getJSONObject("Day");
            int numIcon = day.getInt("Icon");
            newHolder.dayIcon.setImageResource(mContext.getResources().getIdentifier("p" + numIcon, "drawable", mContext.getPackageName()));
            int precipitation = day.getInt("PrecipitationProbability");
            newHolder.dayPrecipitation.setText(String.format("%s%s", String.valueOf(precipitation), newHolder.dayPrecipitation.getText()));
            JSONObject wind = day.getJSONObject("Wind");
            int speed = wind.getJSONObject("Speed").getInt("Value");
            String direction = wind.getJSONObject("Direction").getString("Localized");
            newHolder.dayWind.setText(direction + " ," + speed + " м/с");
            JSONObject night = jsonObj.getJSONObject("Night");
            numIcon = night.getInt("Icon");
            newHolder.nightIcon.setImageResource(mContext.getResources().getIdentifier("p" + numIcon, "drawable", mContext.getPackageName()));
            precipitation = night.getInt("PrecipitationProbability");
            newHolder.nightPrecipitation.setText(String.format("%s%s", String.valueOf(precipitation), newHolder.nightPrecipitation.getText()));
            wind = night.getJSONObject("Wind");
            speed = wind.getJSONObject("Speed").getInt("Value");
            direction = wind.getJSONObject("Direction").getString("Localized");
            newHolder.nightWind.setText(direction + " ," + speed + " м/с");
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return weather.size();
    }
}
