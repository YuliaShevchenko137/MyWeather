package com.shevchenko.yulia.myweather.async;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.shevchenko.yulia.myweather.MainActivity;
import com.shevchenko.yulia.myweather.helper.PageCodeGrabber;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DownloadStartInfo extends AsyncTask<Void, Void, Boolean> implements Task {

    public String getResult() {
        return result;
    }

    private String result;
    private SharedPreferences sharedPreferences;
    private MainActivity activity;

    private final OnTaskCompleteListener mTaskCompleteListener;


    public DownloadStartInfo(MainActivity activity, SharedPreferences sharedPreferences, OnTaskCompleteListener taskCompleteListener) {
        mTaskCompleteListener = taskCompleteListener;
        this.sharedPreferences = sharedPreferences;
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(Boolean bool) {
        if (bool) {
            mTaskCompleteListener.onTaskComplete(this);
        }
    }

    @Override
    protected Boolean doInBackground(Void... arg0) {
        PageCodeGrabber pcg = new PageCodeGrabber();
        try {
            result = pcg.getWeather();
        } catch (IOException e) {
            activity.showDialog();
            return false;
        }
        Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
		String formatedString = dateFormat.format(date);
		try {
            SharedPreferences.Editor ed = sharedPreferences.edit();
            ed.putLong("accuDate", dateFormat.parse(formatedString).getTime());
			ed.putString("accuData", result);
            ed.apply();
		} catch (ParseException ignored) {}
        return true;
    }
}
