package com.appdevelopers.weatherapp;

import java.util.List;

public class WeatherResponse {
    private Main main;
    private List<Weather> weather;
    private String name;

    public Main getMain(){
        return main;
    }

    public List<Weather> getWeather(){
        return weather;
    }

    public String getName(){
        return name;
    }

    public class Main {
        private double temp;
        private double feels_like;
        private double temp_min;
        private double temp_max;
        private int pressure;
        private int humidity;

        public double getTemp() {
            return temp;
        }

        public double getFeels_like() {
            return feels_like;
        }

        public double getTemp_min() {
            return temp_min;
        }

        public double getTemp_max() {
            return temp_max;
        }

        public int getPressure() {
            return pressure;
        }

        public int getHumidity() {
            return humidity;
        }
    }

    public class Weather {
        private String main;
        private String description;
        private String icon;
        public String getMain(){
            return main;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }
}
