package com.appdevelopers.weatherapp;

import com.appdevelopers.weatherapp.Model.FiveDaysWeatherItemModel;

import java.util.List;

public class WeatherResponse {
    private Main main;
    private List<Weather> weather;
    private String name;
    private Sys sys;
    private Wind wind;
    private List<Hourly> hourly; // Added for 3-hour forecast
    private Coord coord;
    private List<FiveDaysWeatherItemModel> list;

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

    public Coord getCoord(){
        return coord;
    }

    public List<FiveDaysWeatherItemModel> getList() {
        return list;
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
            if (speed == 0) return "Calm";
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

    public static class Coord {
        private double lat;
        private double lon;

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }
    }

    public class FiveDaysWeatherItemModel {
        // Define the fields for each weather item, such as temperature, weather description, etc.
        private long dt;             // Unix timestamp of the forecast
        private String dt_txt;       // Formatted date-time string (e.g., "2020-12-16 15:00:00")
        private Main main;           // Main weather data (temperature, humidity, etc.)
        private List<Weather> weather; // List of weather conditions (icon, description, etc.)

        public long getDt() {
            return dt;
        }

        public void setDt(long dt) {
            this.dt = dt;
        }

        public String getDt_txt() {
            return dt_txt;
        }

        public void setDt_txt(String dt_txt) {
            this.dt_txt = dt_txt;
        }

        public Main getMain() {
            return main;
        }

        public void setMain(Main main) {
            this.main = main;
        }

        public List<Weather> getWeather() {
            return weather;
        }

        public void setWeather(List<Weather> weather) {
            this.weather = weather;
        }
    }
}
