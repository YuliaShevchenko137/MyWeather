package com.shevchenko.yulia.myweather.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shevchenko.yulia.myweather.R;
import com.shevchenko.yulia.myweather.adapters.DayWeatherAdapter;
import com.shevchenko.yulia.myweather.async.DownloadStartInfo;
import com.shevchenko.yulia.myweather.async.DownloadWeatherTask;
import com.shevchenko.yulia.myweather.async.OnTaskCompleteListener;
import com.shevchenko.yulia.myweather.async.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.sinopticButton)
    Button sinoptic;
    @BindView(R.id.gismeteoButton)
    Button gismeteo;
    @BindView(R.id.scrappingLoading)
    ProgressBar progressBar;
    @BindView(R.id.buttonPanel)
    LinearLayout buttonPanel;
    @BindView(R.id.my_recycler_view)
    RecyclerView recyclerView;

    SharedPreferences sPref;

    String sinopticString;
    String gismeteoString;

    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView.Adapter adapter;

    public boolean isShowDialog() {
        return isShowDialog;
    }

    boolean isShowDialog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        buttonPanel.setVisibility(View.INVISIBLE);
        sPref = getSharedPreferences("app", MODE_PRIVATE);
        long accuDate = sPref.getLong("accuDate", -1);
        String accuData = sPref.getString("accuData", "");
        if (accuDate == -1 || "".equals(accuData)) {
            downloadStartInfo();
        } else {
            Date date = new Date(accuDate);
            date.setTime(date.getTime() + 1000*60*60*6);
            if (new Date(System.currentTimeMillis()).after(date)) {
                downloadStartInfo();
            } else {
                showInfo(accuData);
            }
        }
        progressBar.setIndeterminate(true);
        long weatherDate = sPref.getLong("weatherDate", -1);
        sinopticString = sPref.getString("sinopticData", "");
        gismeteoString = sPref.getString("gismeteoData", "");
        if (weatherDate == -1 || "".equals(sinopticString) || "".equals(gismeteoString) || "[]".equals(sinopticString) || "[]".equals(gismeteoString)) {
            scrappingInfo();
        } else {
            Date date = new Date(weatherDate);
            date.setTime(date.getTime() + 1000*60*60*3);
            if (new Date(System.currentTimeMillis()).after(date)) {
                scrappingInfo();
            } else {
                showPanel();
            }
        }
        super.onPostCreate(savedInstanceState);
    }


    private void scrappingInfo() {
        DownloadWeatherTask task = new DownloadWeatherTask(this, sPref, this, new OnTaskCompleteListener() {
            @Override
            public void onTaskComplete(Task task) {
                showPanel();
                Toast.makeText(MainActivity.this, "Данные обновлены", Toast.LENGTH_LONG).show();
            }
        });
        task.execute();
    }

    private void showPanel() {
        progressBar.setIndeterminate(false);
        progressBar.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        buttonPanel.setVisibility(View.VISIBLE);
    }

    private void downloadStartInfo() {
        DownloadStartInfo downloadStartInfo = new DownloadStartInfo(this, sPref, new OnTaskCompleteListener() {

            @Override
            public void onTaskComplete(Task task) {
                DownloadStartInfo newTask = (DownloadStartInfo) task;
                showInfo(newTask.getResult());
            }
        });
        downloadStartInfo.execute();

    }

    public void showDialog() {
        isShowDialog = true;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Отсутствует интернет-соединение.\n Будут загружены локальные данные.")
                        .setTitle("Ошибка!");
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }

    private void showInfo(String str) {
        try {
            JSONObject jsonObj = new JSONObject(str);
            JSONArray dailyForecasts = jsonObj.getJSONArray("DailyForecasts");
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(dailyForecasts.getString(0));
            arrayList.add(dailyForecasts.getString(1));
            adapter = new DayWeatherAdapter(MainActivity.this, arrayList);
            recyclerView.setAdapter(adapter);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    @OnClick(R.id.sinopticButton)
    public void openSinopticWeather() {
        showWeather("sinoptic");
    }

    @OnClick(R.id.gismeteoButton)
    public void openGismeteoWeather() {
        showWeather("gismeteo");
    }

    private void showWeather(String companyName) {
        Intent intent = new Intent(this, WeatherActivity.class);
        intent.putExtra("company", companyName);
        startActivity(intent);
    }
}
