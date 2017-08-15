package com.shevchenko.yulia.myweather.helper;

import com.shevchenko.yulia.myweather.helper.parsers.GismeteoParser;
import com.shevchenko.yulia.myweather.helper.parsers.SinopticParser;

import java.io.IOException;
import java.util.Calendar;

public class WeatherData {
    private static WeatherData ourInstance = new WeatherData();

    public static WeatherData getInstance() {
        return ourInstance;
    }

    private WeatherData() {
    }

    public String[] getGismeteoData() throws IOException {
        String[] gismeteoUrls = new String[3];
        String[] gismeteoResult = new String[7];
        gismeteoUrls[0] = "https://www.gismeteo.ua/weather-sumy-4936/hourly";
        gismeteoUrls[1] = "https://www.gismeteo.ua/weather-sumy-4936/3-5-days-hourly";
        gismeteoUrls[2] = "https://www.gismeteo.ua/weather-sumy-4936/5-7-days-hourly";
        for (int count = 0; count < 7; count++) {
            int urlNumber = 1;
            if (count < 3) {
                urlNumber = 0;
            } else if (count > 4) {
                urlNumber = 2;
            }
            gismeteoResult[count] = GismeteoParser.getInstance().getHTMLInfo(gismeteoUrls[urlNumber], count);
        }
        return gismeteoResult;
    }

    public String[] getSinopticData(String string) throws IOException {
        String[] sinopticUrls = new String[7];
        String[] sinopticResult = new String[7];
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            String currentMonth = month/10 + "" + month%10;
            int day = calendar.get(Calendar.DAY_OF_MONTH) + i;
            sinopticUrls[i]= "https://sinoptik.ua/" + string + "/" + year + "-" + currentMonth + "-" + day;
            sinopticResult[i] = SinopticParser.getInstance().getHTMLInfo(sinopticUrls[i]);
        }
        return sinopticResult;
    }
}
