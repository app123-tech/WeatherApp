package com.appdevelopers.weatherapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class NominatimLocation {
    @SerializedName("display_name")
    private String displayName;

    @SerializedName("lat")
    private String lat;

    @SerializedName("lon")
    private String lon;

    @SerializedName("address")
    private Map<String, String> address;

    public String getDisplayName() {
        return displayName;
    }

    public double getLat() {
        try {
            return Double.parseDouble(lat);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public double getLon() {
        try {
            return Double.parseDouble(lon);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public Map<String, String> getAddress() {
        return address;
    }
}
