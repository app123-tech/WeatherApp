package com.appdevelopers.weatherapp;

public class GeoLocationService {
    private String name;
    private String country;
    private String state;

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state != null ? state : ""; // Ensure state is never null
    }
}
