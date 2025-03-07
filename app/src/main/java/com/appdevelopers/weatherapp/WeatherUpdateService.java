package com.appdevelopers.weatherapp;

import android.Manifest;
import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherUpdateService extends IntentService {
    private static final String KEY_CITY = "City";
    private FusedLocationProviderClient fusedLocationProviderClient;
    public WeatherUpdateService() {
        super("WeatherUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (intent != null && intent.hasExtra("LATITUDE") && intent.hasExtra("LONGITUDE") && intent.hasExtra("CITY")) {
            double lat = intent.getDoubleExtra("LATITUDE", 0);
            double lon = intent.getDoubleExtra("LONGITUDE", 0);
            String city = intent.getStringExtra("CITY");

            saveCityToPreferences(city);
            fetchWeatherData(lat, lon);
        } else {
            requestLocation();
        }
    }

        private void saveCityToPreferences(String city) {
            SharedPreferences.Editor editor = getSharedPreferences("WidgetPrefs", MODE_PRIVATE).edit();
            editor.putString(KEY_CITY, city);
            editor.apply();
        }

        private void requestLocation() {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.e("WeatherUpdateService", "Location permissions not granted");
                return;
            }
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Location location = task.getResult();
                        getCityName(location.getLatitude(), location.getLongitude());
                    } else {
                        Log.e("WeatherUpdateService", "Unable to get location");
                    }
                }
            });
        }

        private void getCityName(double latitude, double longitude) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    String cityName = address.getLocality();
                    if (cityName == null || cityName.isEmpty()) {
                        cityName = address.getAdminArea();
                    }
                    saveCityToPreferences(cityName);
                    fetchWeatherData(latitude, longitude);
                } else {
                    Log.e("Geocoder", "No address found");
                    fetchWeatherData(latitude, longitude); // Proceed with coordinates
                }
            } catch (IOException e) {
                Log.e("Geocoder", "Error: " + e.getMessage());
                fetchWeatherData(latitude, longitude); // Proceed with coordinates
            }
        }

    private void fetchWeatherData(double latitude, double longitude) {
        // Fetch weather data from API and save it in WidgetPrefs
        // Assume fetchWeatherFromAPI(latitude, longitude) retrieves weather data and saves it in SharedPreferences
        fetchWeatherFromAPI(latitude, longitude);

        // Notify the widget to update
        Intent intent = new Intent(this, WeatherWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        sendBroadcast(intent);
    }

    private void fetchWeatherFromAPI(double latitude, double longitude) {
        // Fetch weather data using API and store it in "WidgetPrefs"
        SharedPreferences.Editor editor = getSharedPreferences("WidgetPrefs", MODE_PRIVATE).edit();
        editor.putString("Temp", "25°C");
        editor.putString("HighTemp", "H: 28°C");
        editor.putString("LowTemp", "L: 18°C");
        editor.putString("WeatherIcon", "https://example.com/icon.png");
        editor.putString("FeelsLike", "Feels like 26°C");
        editor.apply();
    }
}