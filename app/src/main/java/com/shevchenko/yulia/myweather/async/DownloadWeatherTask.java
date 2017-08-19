package com.shevchenko.yulia.myweather.async;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.widget.Toast;


import com.google.gson.Gson;
import com.shevchenko.yulia.myweather.activities.MainActivity;
import com.shevchenko.yulia.myweather.model.parsers.WeatherData;
import com.shevchenko.yulia.myweather.model.entities.DayWeather;
import com.shevchenko.yulia.myweather.model.parsers.GismeteoParser;
import com.shevchenko.yulia.myweather.model.parsers.SinopticParser;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public final class DownloadWeatherTask extends AsyncTask<Void, String, Boolean> implements Task {
    
    protected final SharedPreferences sPref;

    private String mProgressMessage = "Выполняется загрузка данных";

	private ArrayList<DayWeather> sinopticWeathers = new ArrayList<>();
	private ArrayList<DayWeather> gismeteoWeathers = new ArrayList<>();
	private final OnTaskCompleteListener mTaskCompleteListener;
	private Context context;
	private MainActivity activity;

    @Override
    protected void onPostExecute(Boolean result) {
		mTaskCompleteListener.onTaskComplete(this);
    }

	public DownloadWeatherTask(MainActivity activity, SharedPreferences sPref, Context context, OnTaskCompleteListener taskCompleteListener) {
		this.sPref = sPref;
		mTaskCompleteListener = taskCompleteListener;
		this.context = context;
		Toast.makeText(context, mProgressMessage, Toast.LENGTH_LONG).show();
		this.activity = activity;
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		final boolean[] finishSinoptic = {false};
		final boolean[] finishGismeteo = {false};
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String[] strings = WeatherData.getInstance().getSinopticData("погода-сумы");
					for (int i = 0; i < strings.length; i++) {
						ArrayList<DayWeather> temp = SinopticParser.getInstance().getDayWeather(strings[i], i);
						Collections.addAll(sinopticWeathers, temp.toArray(new DayWeather[temp.size()]));
					}
				} catch (IOException e) {
					if (!activity.isShowDialog()) {
						activity.showDialog();
					}
				} finally {
					finishSinoptic[0] = true;
				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String[] strings = WeatherData.getInstance().getGismeteoData();
					for (String string : strings) {
						ArrayList<DayWeather> temp = GismeteoParser.getInstance().getDayWeather(string);
						Collections.addAll(gismeteoWeathers, temp.toArray(new DayWeather[temp.size()]));
					}
				} catch (IOException e) {
					if (!activity.isShowDialog()) {
						activity.showDialog();
					}
				} finally {
					finishGismeteo[0] = true;
				}

			}
		}).start();
		while(!finishGismeteo[0] || !finishSinoptic[0] ){}
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        String formatedString = dateFormat.format(date);
        try {
            SharedPreferences.Editor ed = sPref.edit();
            ed.putLong("weatherDate", dateFormat.parse(formatedString).getTime());
            ed.putString("sinopticData", new Gson().toJson(sinopticWeathers));
            ed.putString("gismeteoData", new Gson().toJson(gismeteoWeathers));
            ed.apply();
        } catch (ParseException ignored) {}
		return true;
	}
}