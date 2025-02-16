package com.appdevelopers.weatherapp;

import java.util.List;

public class AqiResponse {
    private List<AirQuality> list;

    public List<AirQuality> getList() {
        return list;
    }

    public static class AirQuality {
        private Main main;

        public Main getMain() {
            return main;
        }

        public static class Main {
            private int aqi;

            public int getAqi() {
                return aqi;
            }
        }
    }
}
