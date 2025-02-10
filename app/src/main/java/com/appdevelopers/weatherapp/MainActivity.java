package com.appdevelopers.weatherapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.appdevelopers.weatherapp.Fragment.FragmentTenDays;
import com.appdevelopers.weatherapp.Fragment.FragmentTodayActivity;
import com.appdevelopers.weatherapp.Fragment.FragmentTomorrow;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Response;
import retrofit2.Callback;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {
    private static final String API_KEY = "2e20e6e6c10856f71e0312f1ca503ac9";
    private static final String PREFS_NAME = "WeatherAppPrefs";
    private static final String KEY_CITY = "City";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private AppCompatButton buttonToday, buttonTomorrow, buttonTenDays;
    private ImageView imageViewSearch, imageViewSetting, imageViewSun;
    private TextView textViewCityName, textViewTempTitle, textViewHigh, textViewLow, textViewTemp;
    private SharedPreferences sharedPreferences;
    private String selectedCity = "Kathmandu"; //Default City
    private FusedLocationProviderClient fusedLocationProviderClient;

    private void setButtonColors(AppCompatButton selectedButton){
        buttonToday.setBackgroundTintList(getResources().getColorStateList(R.color.light));
        buttonTomorrow.setBackgroundTintList(getResources().getColorStateList(R.color.light));
        buttonTenDays.setBackgroundTintList(getResources().getColorStateList(R.color.light));

        buttonToday.setTextColor(getResources().getColor(R.color.blue));
        buttonTomorrow.setTextColor(getResources().getColor(R.color.blue));
        buttonTenDays.setTextColor(getResources().getColor(R.color.blue));

        selectedButton.setBackgroundTintList(getResources().getColorStateList(R.color.blue));
        selectedButton.setTextColor(getResources().getColor(R.color.light));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        if (!NetworkUtils.isConnected(this)) {
            Intent intent = new Intent(MainActivity.this, NoInternetActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        buttonToday = findViewById(R.id.buttonToday);
        buttonTomorrow = findViewById(R.id.buttonTomorrow);
        buttonTenDays = findViewById(R.id.buttonTenDays);
        imageViewSearch = findViewById(R.id.imageViewSearch);
        imageViewSetting = findViewById(R.id.imageViewSetting);
        imageViewSun = findViewById(R.id.imageViewSun);
        textViewCityName = findViewById(R.id.textViewCityName);
        textViewTempTitle = findViewById(R.id.textViewTempTitle);
        textViewHigh = findViewById(R.id.textViewHigh);
        textViewLow = findViewById(R.id.textViewLow);
        textViewTemp = findViewById(R.id.textViewTemp);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        selectedCity = sharedPreferences.getString(KEY_CITY, "Kathmandu");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocation();

        setButtonColors(buttonToday);

        imageViewSetting.setOnClickListener(v -> {
          Intent intent = new Intent(MainActivity.this, Setting.class);
          startActivity(intent);
        });

        imageViewSearch.setOnClickListener(v ->{
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        buttonToday.setOnClickListener(v -> {
            loadFragment(new FragmentTodayActivity());
            setButtonColors(buttonToday);
        });

        buttonTomorrow.setOnClickListener(v -> {
            loadFragment(new FragmentTomorrow());
            setButtonColors(buttonTomorrow);
        });

        buttonTenDays.setOnClickListener(v -> {
            loadFragment(new FragmentTenDays());
            setButtonColors(buttonTenDays);
        });

        if (savedInstanceState == null) {
            loadFragment(new FragmentTodayActivity());
        }
    }

    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void requestLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>(){
            @Override
            public void onComplete(@NonNull Task<Location> task){
                if (task.isSuccessful() && task.getResult() != null){
                    Location location = task.getResult();
                    getCityName(location.getLatitude(), location.getLongitude());
                } else {
                    Toast.makeText(MainActivity.this, "Unable to get location", Toast.LENGTH_SHORT).show();
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

                String fullAddress = address.getAddressLine(0);
                String subLocality = address.getSubLocality();
                String city = address.getLocality();

                if (fullAddress != null && !fullAddress.isEmpty()){
                    selectedCity = fullAddress;
                } else if (subLocality != null && !subLocality.isEmpty()) {
                    selectedCity = subLocality + ", " + city;
                } else {
                    selectedCity = city;
                }

                textViewCityName.setText(selectedCity);
                fetchWeatherData(selectedCity);
            }
        } catch (IOException e) {
            Log.e("Geocoder", "Error getting city name: " + e.getMessage());
            textViewCityName.setText("Unknown Location");
        }
    }

    private void fetchWeatherData(String city){
        OpenWeatherMapService apiService = ApiClient.getClient().create(OpenWeatherMapService.class);
        Call<WeatherResponse> call = apiService.getCurrentWeather(city, API_KEY, "metric");

        call.enqueue(new Callback<WeatherResponse>(){
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response){
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherData = response.body();

                    textViewCityName.setText(weatherData.getName());
                    textViewTemp.setText(String.format("%.1f°C", weatherData.getMain().getTemp()));
                    textViewHigh.setText(String.format("H: %.1f°C", weatherData.getMain().getTemp_max()));
                    textViewLow.setText(String.format("L: %.1f°C", weatherData.getMain().getTemp_min()));

                    sharedPreferences.edit().putString(KEY_CITY, city).apply();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to retrieve weather data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<WeatherResponse> call, Throwable t) {
                Log.e("WeatherApi", "Error fetching data: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        }
    }
}