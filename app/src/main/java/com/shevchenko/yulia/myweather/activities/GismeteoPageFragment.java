package com.shevchenko.yulia.myweather.activities;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shevchenko.yulia.myweather.R;
import com.shevchenko.yulia.myweather.adapters.CurrentWeatherAdapter;
import com.shevchenko.yulia.myweather.model.entities.DayWeather;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class GismeteoPageFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    int pageNumber;
    static ArrayList<DayWeather> weatherList = null;
    @BindView(R.id.list)
    ListView list;

    static SharedPreferences sPref;

    static GismeteoPageFragment newInstance(int page, WeatherActivity activity) {
        GismeteoPageFragment gismeteoPageFragment = new GismeteoPageFragment();
        sPref = activity.getSharedPreferences("app", MODE_PRIVATE);
        weatherList = new Gson().fromJson(sPref.getString("gismeteoData", ""), new TypeToken<ArrayList<DayWeather>>(){}.getType());
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        gismeteoPageFragment.setArguments(arguments);
        return gismeteoPageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.day_scraping_weather, null);
        ButterKnife.bind(this, view);
        CurrentWeatherAdapter adapter = new CurrentWeatherAdapter(this.getContext(), weatherList.get(pageNumber).getList());
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
    }
}