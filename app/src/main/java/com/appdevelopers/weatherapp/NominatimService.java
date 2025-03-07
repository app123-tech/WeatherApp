package com.appdevelopers.weatherapp;

import android.location.Location;

import com.appdevelopers.weatherapp.Model.NominatimLocation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NominatimService {
    @GET("search?format=json&addressdetails=1&limit=10")
    Call<List<NominatimLocation>> getLocations(@Query("q") String query);
}
