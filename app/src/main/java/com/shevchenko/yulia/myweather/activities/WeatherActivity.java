package com.shevchenko.yulia.myweather.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shevchenko.yulia.myweather.R;
import com.shevchenko.yulia.myweather.model.entities.DayWeather;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherActivity extends AppCompatActivity {

    @BindView(R.id.company_name)
    TextView companyName;
    @BindView(R.id.pager)
    ViewPager pager;

    SharedPreferences sPref;
    PagerAdapter pagerAdapter;

    ArrayList<DayWeather> weathers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);
        ButterKnife.bind(this);
        sPref = getSharedPreferences("app", MODE_PRIVATE);
        weathers = new Gson().fromJson(sPref.getString("sinopticData", ""), new TypeToken<ArrayList<DayWeather>>(){}.getType());
        companyName.setText(String.format("Данные представлены компанией %s", getIntent().getStringExtra("company").toUpperCase()));
        if ("sinoptic".equals(getIntent().getStringExtra("company"))){
            pagerAdapter = new MySinopticFragmentPagerAdapter(getSupportFragmentManager());
            pager.setAdapter(pagerAdapter);
            pagerAdapter.notifyDataSetChanged();
        } else {
            pagerAdapter = new MyGismeteoFragmentPagerAdapter(getSupportFragmentManager());
            pager.setAdapter(pagerAdapter);
            pagerAdapter.notifyDataSetChanged();
        }

    }

    private class MySinopticFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public MySinopticFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return weathers.get(position).getDate();
        }

        @Override
        public Fragment getItem(int position) {
            return SinopticPageFragment.newInstance(position, WeatherActivity.this);
        }

        @Override
        public int getCount() {
            return weathers.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    private class MyGismeteoFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public MyGismeteoFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return weathers.get(position).getDate();
        }

        @Override
        public Fragment getItem(int position) {
            return GismeteoPageFragment.newInstance(position, WeatherActivity.this);
        }

        @Override
        public int getCount() {
            return weathers.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
