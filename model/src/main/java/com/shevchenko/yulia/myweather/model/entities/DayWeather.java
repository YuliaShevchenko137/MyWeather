package com.shevchenko.yulia.myweather.model.entities;

import java.util.ArrayList;

public class DayWeather {
    String date;
    ArrayList<Weather> list;

    public DayWeather(String date, ArrayList<Weather> list) {
        this.date = date;
        this.list = list;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DayWeather that = (DayWeather) o;

        return date.equals(that.date) && list.equals(that.list);

    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + list.hashCode();
        return result;
    }

    public ArrayList<Weather> getList() {
        return list;
    }

    public void setList(ArrayList<Weather> list) {
        this.list = list;
    }
}
