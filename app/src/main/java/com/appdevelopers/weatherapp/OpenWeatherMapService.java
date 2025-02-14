package com.appdevelopers.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapService {
    @GET("data/2.5/weather")
    Call<WeatherResponse> getCurrentWeatherByCoords(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
           // @Query("q") String city,
            @Query("appid") String apiKey,
            @Query("units") String units
    );

    Call<WeatherResponse> getCurrentWeather(String kathmandu, String apiKey, String metric);
}
