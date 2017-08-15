package com.shevchenko.yulia.myweather.helper.parsers;

import com.shevchenko.yulia.myweather.helper.entities.Weather;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SinopticParser {
    private static SinopticParser ourInstance = new SinopticParser();

    public static SinopticParser getInstance() {
        return ourInstance;
    }

    private SinopticParser() {
    }

    int countInterval = 0;

    public String getHTMLInfo(String urlString) throws IOException {
        StringBuilder res1 = new StringBuilder();
        final String userAgent = "Mozilla/5.0 (X11; U; Linux i586; en-US; rv:1.7.3) Gecko/20040924 Epiphany/1.4.4 (Ubuntu)";
        Document doc = Jsoup.connect(urlString) // you get a 'Connection' object here
                    .userAgent(userAgent) // ! set the user agent
                    .timeout(5 * 1000) // set timeout
                    .get();
            res1.append(doc.outerHtml());
        return res1.toString().split("<tr class=\"gray time\"> ")[1].split("<div class=\"wDescription clearfix\">")[0];
    }

    public ArrayList<Weather> getDayWeather(String data, int day){
        ArrayList<Weather> dayWeather = new ArrayList<>();
        String[] categories = data.split("</tr> ");
        String[] date = parseData(categories[0], day);
        String[] urls = parseUrls(categories[1]);
        int[] temperature = parseTemperature(categories[2]);
        int[] pressure = parsePressure(categories[4]);
        int[] humidity = parseHumidity(categories[5]);
        String[] wind = parseWind(categories[6]);
        for (int i = 0; i < countInterval; i++) {
            SimpleDateFormat format = new SimpleDateFormat();
            format.applyPattern("yyyy-MM-dd H :mm");
            Date thisDate;
            try {
                thisDate = format.parse(date[i]);
            } catch (ParseException e) {
                thisDate = new Date(System.currentTimeMillis());
            }
            if (new Date(System.currentTimeMillis()).before(thisDate)) {
                dayWeather.add(new Weather(date[i], urls[i], temperature[i], pressure[i], humidity[i], wind[i]));
            }
        }
        return dayWeather;
    }

    private String[] parseData(String string, int day) {
        String[] parts = string.split("\">");
        countInterval = parts.length - 1;
        String[] result = new String[countInterval];
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String currentMonth = month/10 + "" + month%10;
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH) + day;
        for (int i = 0; i < countInterval; i++) {
            result[i] = year + "-" + currentMonth + "-" + thisDay + " " + parts[i+1].split(" </td>")[0];
        }
        return result;
    }

    private String[] parseWind(String string) {
        String[] parts = string.split("data-tooltip=\"");
        String[] result = new String[countInterval];
        for (int i = 1; i < parts.length; i++) {
            result[i-1] = parts[i].split("\" class=\"")[0];
        }
        return result;
    }

    private String[] parseUrls(String string) {
        String[] parts = string.split(" src=\"//");
        String[] result = new String[countInterval];
        for (int i = 1; i < parts.length; i++) {
            result[i-1] = parts[i].split("\" alt=\"\">")[0];
        }
        return result;
    }

    private int[] parseHumidity(String string) {
        String[] parts = string.split("\">");
        int[] res = new int[countInterval];
        for (int i = 0; i < countInterval; i++) {
            res[i] = Integer.parseInt(parts[i+1].split("</td>")[0]);
        }
        return res;
    }

    private int[] parsePressure(String string) {
        String[] parts = string.split("\">");
        int[] res = new int[countInterval];
        for (int i = 0; i < countInterval; i++) {
            res[i] = Integer.parseInt(parts[i+2].split("</td>")[0]);
        }
        return res;
    }

    private int[] parseTemperature(String string) {
        String[] parts = string.split("\">");
        int[] res = new int[countInterval];
        for (int i = 0; i < countInterval; i++) {
            String part = parts[i+2].split("</td>")[0].substring(0, 3);
            res[i] = Integer.parseInt(part);
        }
        return res;
    }
}
