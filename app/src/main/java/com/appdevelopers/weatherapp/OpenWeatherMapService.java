package com.appdevelopers.weatherapp;

import com.appdevelopers.weatherapp.Model.GeoLocation;

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

    @GET("data/2.5/weather")
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

    @GET("data/2.5/forecast")
    Call<WeatherResponse> getWeatherByCoordinates(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
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
    Call<List<GeoLocation>> getLocations(
            @Query("q") String query,
            @Query("limit") int limit,
            @Query("appid") String appid
    );

    // Current weather API endpoint to get the main city name.
    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeather(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String appid
    );

    @GET("data/2.5/forecast")
    Call<WeatherResponse> getFiveDayForecast(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("appid") String apikey,
            @Query("units") String units
    );
}
