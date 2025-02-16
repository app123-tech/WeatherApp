package com.appdevelopers.weatherapp;

import java.util.List;

public class WeatherResponse {
    private Main main;
    private List<Weather> weather;
    private String name;
    private Sys sys;
    private Wind wind;
    private List<Hourly> hourly; // Added for 3-hour forecast

    public Main getMain() {
        return main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public String getName() {
        return name;
    }

    public Sys getSys() {
        return sys;
    }

    public Wind getWind() {
        return wind;
    }

    public List<Hourly> getHourly() {
        return hourly;
    }

    // Wind class to hold wind data
    public static class Wind {
        private double speed;
        private int deg;

        public double getSpeed() {
            return speed;
        }

        public int getDeg() {
            return deg;
        }

        public String getDirection() {
            if (deg >= 0 && deg < 45) return "N";
            if (deg >= 45 && deg < 90) return "NE";
            if (deg >= 90 && deg < 135) return "E";
            if (deg >= 135 && deg < 180) return "SE";
            if (deg >= 180 && deg < 225) return "S";
            if (deg >= 225 && deg < 270) return "SW";
            if (deg >= 270 && deg < 315) return "W";
            return "NW";
        }
    }

    // Sys class for sunrise and sunset times
    public static class Sys {
        private long sunrise;
        private long sunset;

        public long getSunrise() {
            return sunrise;
        }

        public long getSunset() {
            return sunset;
        }
    }

    // Main class for temperature, humidity, pressure, etc.
    public static class Main {
        private double temp;
        private double feels_like;
        private int humidity;
        private double temp_max; // High temperature
        private double temp_min; // Low temperature

        public double getTemp() {
            return temp;
        }

        public double getFeels_like() {
            return feels_like;
        }

        public int getHumidity() {
            return humidity;
        }

        public double getTemp_max() {
            return temp_max;
        }

        public double getTemp_min() {
            return temp_min;
        }
    }

    // Weather class for description of weather conditions
    public static class Weather {
        private String main;
        private String description;
        private String icon;

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }

    // Hourly forecast data class
    public static class Hourly {
        private Main main;
        private List<Weather> weather;
        private String dt_txt;

        public Main getMain() {
            return main;
        }

        public List<Weather> getWeather() {
            return weather;
        }

        public String getDt_txt() {
            return dt_txt;
        }
    }
}
