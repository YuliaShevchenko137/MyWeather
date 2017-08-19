package com.shevchenko.yulia.myweather.model.entities;

public class Weather {

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;
    private String url;
    private int temperature;
    private int pressure;
    private int humidity;
    private String wind;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public Weather(String time, String url, int temperature, int pressure, int humidity, String wind) {
        this.time = time;
        this.url = url;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.wind = wind;
    }

    public Weather(String url, int temperature, int pressure, int humidity, String wind) {

        this.url = url;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.wind = wind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weather weather = (Weather) o;
        return temperature == weather.temperature
                && pressure == weather.pressure
                && humidity == weather.humidity
                && time.equals(weather.time)
                && url.equals(weather.url)
                && wind.equals(weather.wind);

    }

    @Override
    public int hashCode() {
        int result = time.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + temperature;
        result = 31 * result + pressure;
        result = 31 * result + humidity;
        result = 31 * result + wind.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "date='" + time + '\'' +
                ", url='" + url + '\'' +
                ", temperature=" + temperature +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", wind='" + wind + '\'' +
                '}';
    }
}
