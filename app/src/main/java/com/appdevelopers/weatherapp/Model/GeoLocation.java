package com.appdevelopers.weatherapp.Model;

public class GeoLocation {
    private String name;
    private String country;
    private String state;
    private double lat; // Added latitude field
    private double lon; // Added longitude field

    public GeoLocation(double lat, double lon, String name, String country, String state) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.country = country;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
