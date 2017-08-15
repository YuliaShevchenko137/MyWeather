package com.shevchenko.yulia.myweather;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shevchenko.yulia.myweather.helper.entities.Weather;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherActivity extends AppCompatActivity {

    TextView companyName;
    ArrayList<Weather> weathers;
    WeatherAdapter adapter;
    @BindView(R.id.list)
    ListView lvMain;
    SharedPreferences sPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);
        companyName = (TextView) findViewById(R.id.company_name);
        ButterKnife.bind(this);
        sPref = getSharedPreferences("app", MODE_PRIVATE);
        companyName.setText(getIntent().getStringExtra("company"));
        if ("sinoptic".equals(companyName.getText())){
            weathers = new Gson().fromJson(sPref.getString("sinopticData", ""), new TypeToken<ArrayList<Weather>>(){}.getType());
        } else {
            weathers = new Gson().fromJson(sPref.getString("gismeteoData", ""), new TypeToken<ArrayList<Weather>>(){}.getType());
        }
        adapter = new WeatherAdapter(this, this, weathers);
        lvMain.setAdapter(adapter);
    }
}
