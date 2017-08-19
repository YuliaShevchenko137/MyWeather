package com.shevchenko.yulia.myweather.model.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PageCodeGrabber {

    private String adr = "http://dataservice.accuweather.com/forecasts/v1/daily/5day/325825?apikey=kFgJD0DZOzMeonFHXmZZZGTUaEuaJ5Ig&language=ru&details=true&metric=true";

    public String getWeather() throws IOException {
        URL url = new URL(adr); //создаем URL
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "UTF-8")
            );
            StringBuilder text = new StringBuilder();
            String str ;
            while((str = br.readLine()) != null){ // пока не достигнут конец, считываем страницу построчно
                text.append(str);
            }
            return text.toString();
    }
}
