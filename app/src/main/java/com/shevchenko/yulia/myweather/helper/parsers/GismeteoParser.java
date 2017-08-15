package com.shevchenko.yulia.myweather.helper.parsers;

import com.shevchenko.yulia.myweather.helper.entities.Weather;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GismeteoParser {
    private static GismeteoParser ourInstance = new GismeteoParser();

    public static GismeteoParser getInstance() {
        return ourInstance;
    }

    private GismeteoParser() {
    }

    public String getHTMLInfo(String urlString, int count) throws IOException {
        String result = "";
        final String userAgent = "Mozilla/5.0 (X11; U; Linux i586; en-US; rv:1.7.3) Gecko/20040924 Epiphany/1.4.4 (Ubuntu)";
        Document doc = Jsoup.connect(urlString) // you get a 'Connection' object here
                    .userAgent(userAgent) // ! set the user agent
                    .timeout(5 * 1000) // set timeout
                    .get();
        result = doc.outerHtml();
        result = result.split("<div class=\"wsection wdata\">")[1].substring(12).split("<div class=\"section bottom\">")[0];
        String res;
        if (count != 0) {
            res = result.split("<tbody id=\"tbwdaily1\">")[0];
            if (count %2 == 1) {
                res = res + "<tbody id=\"tbwdaily2\">" + result.split("<tbody id=\"tbwdaily2\" style=\"display: none;\">")[1].split("<tbody id=\"tbwdaily3\" style=\"display: none;\">")[0] + "</table>";
            } else {
                res = res + "<tbody id=\"tbwdaily3\">" + result.split("<tbody id=\"tbwdaily3\" style=\"display: none;\">")[1];
            }
        } else {
            res = result.split("<tbody id=\"tbwdaily2\" style=\"display: none;\">")[0] + "</table>";
        }
        return res;
    }

    public ArrayList<Weather> getDayWeather(String data){
        String[] hours = data.split("<tr class=\"wrow");
        ArrayList<Weather> weathers = new ArrayList<>();
        for (int i = 1; i < hours.length; i++) {
            String[] lines = hours[i].split("<td");
            String part1 = lines[0].split("\"> ")[1];
            String date = part1.split("Local: ")[1];
            SimpleDateFormat format = new SimpleDateFormat();
            format.applyPattern("yyyy-MM-dd H:mm");
            Date thisDate;
            try {
                thisDate = format.parse(date);
            } catch (ParseException e) {
                thisDate = new Date(System.currentTimeMillis());
            }
            if (new Date(System.currentTimeMillis()).after(thisDate)) {
                continue;
            }
            String url = lines[1].split(" src=\"//")[1].split("\" alt=\"")[0];
            int temperature = parseTemperature(lines[3]);
            int pressure = parsePressure(lines[4]);
            String wind = parseWind(lines[5]);
            int humidity = parseHumidity(lines[6]);
            weathers.add(new Weather(date, url, temperature, pressure, humidity, wind));
        }
        return weathers;
    }

    private String parseWind(String string) {
        return string.split(" title=\"")[1].split("\">")[0]
                + ", "
                + string.split("m_wind ms\">")[1].split("</span>")[0]
                + " м/с";

    }

    private int parseHumidity(String string) {
        return Integer.parseInt(string.split(">")[1].split("</td")[0]);
    }

    private int parsePressure(String string) {
        return Integer.parseInt(string.split("m_press torr\">")[1].split("</span>")[0]);
    }

    private int parseTemperature(String string) {
        return Integer.parseInt(string.split("m_temp c\">")[1].split("</span>")[0]);
    }

}
