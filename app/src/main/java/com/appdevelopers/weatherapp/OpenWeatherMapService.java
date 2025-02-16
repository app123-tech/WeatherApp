package com.appdevelopers.weatherapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapService {
    @GET("data/2.5/weather")
    Call<WeatherResponse> getCurrentWeatherByCoords(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            //@Query("q") String city,
            @Query("appid") String apiKey,
            @Query("units") String units
    );

    @GET("data/2.5/weather")        // Fetch 5-day forecast in 3-hour intervals
    Call<WeatherResponse> getCurrentWeatherByCity(
            @Query("q") String city,
            @Query("appid") String apiKey,
            @Query("units") String units
    );

    @GET("data/2.5/forecast")
    Call<WeatherResponse> getFiveDayForecastByCity(
            @Query("q") String city,
            @Query("appid") String apiKey,
            @Query("units") String units
    );

    @GET("data/2.5/air_pollution")
    Call<AqiResponse> getAirQualityIndex(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("appid") String apiKey
    );

    @GET("geo/1.0/direct")
    Call<List<GeoLocationService>> getLocations(
            @Query("q") String city,
            @Query("limit") int limit,
            @Query("appid") String apiKey
    );

}
