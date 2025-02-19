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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.appdevelopers.weatherapp.Fragment.FragmentFiveDays;
import com.appdevelopers.weatherapp.Fragment.FragmentTodayActivity;
import com.appdevelopers.weatherapp.Fragment.FragmentTomorrow;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Response;
import retrofit2.Callback;
import retrofit2.Call;

public class MainActivity extends BaseActivity {
    private static final String API_KEY = "151be366b244fa29f3132ffb1b84453d";
    private static final String PREFS_NAME = "WeatherAppPrefs";
    private static final String KEY_CITY = "City";
    private static final String KEY_HIGH_TEMP = "HighTemp";
    private static final String KEY_LOW_TEMP = "LowTemp";
    private static final String KEY_LAST_UPDATED = "LastUpdated";
    private String lastCity = "";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private AppCompatButton buttonToday, buttonTomorrow;
    private ImageView imageViewSearch, imageViewSetting, imageViewIcon;
    private TextView textViewCityName, textViewFeelsLike, textViewHighTemp, textViewLowTemp, textViewTemp;
    private SharedPreferences sharedPreferences;
    private String selectedCity = "Kathmandu";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double dailyHighTemp = Double.MIN_VALUE; // Track the highest temperature of the day
    private double dailyLowTemp = Double.MAX_VALUE;  // Track the lowest temperature of the day
    private long lastResetTime = 0; // Track the last time high and low were reset
    private boolean isLanguageChange = false;

    private void setButtonColors(AppCompatButton selectedButton) {
        buttonToday.setBackgroundTintList(getResources().getColorStateList(R.color.light));
        buttonTomorrow.setBackgroundTintList(getResources().getColorStateList(R.color.light));
       // buttonTenDays.setBackgroundTintList(getResources().getColorStateList(R.color.light));

        buttonToday.setTextColor(getResources().getColor(R.color.blue));
        buttonTomorrow.setTextColor(getResources().getColor(R.color.blue));
       // buttonTenDays.setTextColor(getResources().getColor(R.color.blue));

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

        sharedPreferences = getSharedPreferences("AppSetting", MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceListener);

        if (getIntent().getBooleanExtra("isLanguageChange", false)) {
            isLanguageChange = true;
        }

        buttonToday = findViewById(R.id.buttonToday);
        buttonTomorrow = findViewById(R.id.buttonTomorrow);
       // buttonTenDays = findViewById(R.id.buttonTenDays);
        imageViewSearch = findViewById(R.id.imageViewSearch);
        imageViewSetting = findViewById(R.id.imageViewSetting);
        imageViewIcon = findViewById(R.id.imageViewIcon);
        textViewCityName = findViewById(R.id.textViewCityName);
        textViewFeelsLike = findViewById(R.id.textViewFeelsLike);
        textViewHighTemp = findViewById(R.id.textViewHighTemp);
        textViewLowTemp = findViewById(R.id.textViewLowTemp);
        textViewTemp = findViewById(R.id.textViewTemp);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        selectedCity = sharedPreferences.getString(KEY_CITY, "Kathmandu");
        lastCity = selectedCity;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (getIntent().hasExtra("LATITUDE") &&
                getIntent().hasExtra("LONGITUDE") &&
                getIntent().hasExtra("CITY")) {

            double lat = getIntent().getDoubleExtra("LATITUDE", 0);
            double lon = getIntent().getDoubleExtra("LONGITUDE", 0);
            String city = getIntent().getStringExtra("CITY");

            // Update UI with the passed city name.
            textViewCityName.setText(city);
            // Fetch weather data for the selected location.
            fetchWeatherData(lat, lon, city);
        } else {
            // No extras provided: use device location.
            requestLocation();
        }

        setButtonColors(buttonToday);
        loadHighAndLow();

        imageViewSetting.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Setting.class);
            startActivity(intent);
        });

        imageViewSearch.setOnClickListener(v -> {
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


        if (savedInstanceState == null) {
            loadFragment(new FragmentTodayActivity());
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
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

                String subLocality = address.getSubLocality(); // Sub-location (e.g., Tripureshower, Phewa Lake)
                String locality = address.getLocality(); // City name (e.g., Kathmandu, Pokhara)
                String adminArea = address.getAdminArea(); // State/Province name (e.g., Bagmati, Gandaki)
                String country = address.getCountryName(); // Country name (e.g., Nepal)

                String cityName = "";
                if (subLocality != null && !subLocality.isEmpty()) {
                    cityName = subLocality + ", " + locality; // Combine sub-locality with city
                } else if (locality != null && !locality.isEmpty()) {
                    cityName = locality; // Use city name only
                } else if (adminArea != null && !adminArea.isEmpty()) {
                    cityName = adminArea; // Fallback to state if city is unavailable
                } else {
                    cityName = country; // Fallback to country if nothing else is found
                }
                selectedCity = cityName;
            } else {
                Log.e("Geocoder", "Unknown location. Using fallback: Kathmandu");
                selectedCity = "Kathmandu";
            }
        } catch (IOException e) {
            Log.e("Geocoder", "Error getting city name: " + e.getMessage());
            selectedCity = "Kathmandu";
        }
        textViewCityName.setText(selectedCity);
        fetchWeatherData(latitude, longitude, selectedCity); // Fetch weather
    }

    private void fetchWeatherData(double latitude, double longitude, String city) {
        Log.d("WeatherApi", "Fetching weather data for: " + city + " (" + latitude + ", " + longitude + ")");
        OpenWeatherMapService apiService = ApiClient.getClient().create(OpenWeatherMapService.class);
        Call<WeatherResponse> call = apiService.getCurrentWeatherByCoords(latitude, longitude, API_KEY, "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherData = response.body();
                    updateUI(weatherData);
                    sharedPreferences.edit().putString(KEY_CITY, city).apply();
                } else {
                    Log.e("WeatherApi", "Unknown location error: " + response.message());
                    fetchWeatherByCity(city); // Fallback to city data
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("WeatherApi", "Error fetching data: " + t.getMessage());
                fetchWeatherByCity(city);
            }
        });
    }

    private String getTemperatureUnit() {
        SharedPreferences appSettings = getSharedPreferences("AppSetting", MODE_PRIVATE);
        return appSettings.getString("TemperatureUnit", "C"); // Default to Celsius if not set
    }

    private void fetchWeatherByCity(String city) {
        Log.d("WeatherApi", "Fetching weather data for city: " + city);
        OpenWeatherMapService apiService = ApiClient.getClient().create(OpenWeatherMapService.class);
        Call<WeatherResponse> call = apiService.getCurrentWeatherByCity(city, API_KEY, "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                    selectedCity = city;
                    // textViewCityName.setText(selectedCity);
                    //sharedPreferences.edit().putString(KEY_CITY, selectedCity).apply();
                } else {
                    Log.e("WeatherApi", "City data unavailable. Trying nearest location...");
                    fetchWeatherFallback();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("WeatherApi", "Error fetching city data: " + t.getMessage());
                fetchWeatherFallback();
            }
        });
    }

    private void fetchWeatherFallback() {
        Log.d("WeatherApi", "Fetching fallback weather data for Kathmandu");
        fetchWeatherByCity("Kathmandu"); // Default fallback location
    }

    // Function to update UI with weather data
    private void updateUI(WeatherResponse weatherData) {
        runOnUiThread(() -> {
            Log.d("WeatherApi", "API Response: " + new Gson().toJson(weatherData));
            Log.d("WeatherData", "Temp: " + weatherData.getMain().getTemp());
            Log.d("WeatherData", "Temp Max: " + weatherData.getMain().getTemp_max());
            Log.d("WeatherData", "Temp Min: " + weatherData.getMain().getTemp_min());
            Log.d("WeatherData", "Feels Like: " + weatherData.getMain().getFeels_like());

            String tempUnit = getTemperatureUnit();

            // Get current temperature
            double temp = weatherData.getMain().getTemp();
           // double highTemp = weatherData.getMain().getTemp_max();
           // double lowTemp = weatherData.getMain().getTemp_min();
            double apiHigh = weatherData.getMain().getTemp_max();
            double apiLow = weatherData.getMain().getTemp_min();

            if (tempUnit.equals("F")) {
                temp = (temp * 9 / 5) + 32;
              //  highTemp = (highTemp * 9 / 5) + 32;
              //  lowTemp = (lowTemp * 9 / 5) + 32;
                apiHigh = (apiHigh * 9 / 5) + 32;
                apiLow = (apiLow * 9 / 5) + 32;
            }

            double displayHigh, displayLow;
            // Check if a new day has started
            if (Math.abs(apiHigh - apiLow) < 0.1) {
                // Fall back to manual tracking if API values appear identical.
                if (!isLanguageChange && (isNewDay() || !selectedCity.equals(lastCity))) {
                    resetHighAndLow(temp);
                    lastCity = selectedCity; // update the lastCity to the new location
                }

                // Update manual high/low in Celsius
                if (temp > dailyHighTemp) {
                    dailyHighTemp = temp;
                }
                if (temp < dailyLowTemp) {
                    dailyLowTemp = temp;
                }
                // Convert stored Celsius values to display unit if needed
                if (tempUnit.equals("F")) {
                    temp = (temp * 9 / 5) + 32;
                    displayHigh = (dailyHighTemp * 9 / 5) + 32;
                    displayLow = (dailyLowTemp * 9 / 5) + 32;
                } else {
                    temp = temp;
                    displayHigh = dailyHighTemp;
                    displayLow = dailyLowTemp;
                }
            } else {
                // Use the API values directly
                if (tempUnit.equals("F")) {
                    temp = (temp * 9 / 5) + 32;
                    displayHigh = (apiHigh * 9 / 5) + 32;
                    displayLow = (apiLow * 9 / 5) + 32;
                } else {
                    temp = temp;
                    displayHigh = apiHigh;
                    displayLow = apiLow;
                }
            }

            textViewHighTemp.setText(String.format("H: %.1f%s", displayHigh, tempUnit.equals("C") ? "°C" : "°F"));
            textViewLowTemp.setText(String.format("L: %.1f%s", displayLow, tempUnit.equals("C") ? "°C" : "°F"));
            textViewTemp.setText(String.format("%.1f%s", temp, tempUnit.equals("C") ? "°C" : "°F"));
            textViewFeelsLike.setText(weatherData.getWeather().get(0).getDescription());

            if (weatherData.getWeather() != null && !weatherData.getWeather().isEmpty()) {
                String iconCode = weatherData.getWeather().get(0).getIcon();
                String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
                Glide.with(MainActivity.this).load(iconUrl).into(imageViewIcon);
            }
            saveHighAndLow();
        });
    }

    private boolean isNewDay() {
        Calendar today = Calendar.getInstance();
        Calendar lastResetDate = Calendar.getInstance();
        lastResetDate.setTimeInMillis(lastResetTime);

        // Check if the year, month, and day are different
        return today.get(Calendar.YEAR) != lastResetDate.get(Calendar.YEAR) ||
                today.get(Calendar.MONTH) != lastResetDate.get(Calendar.MONTH) ||
                today.get(Calendar.DAY_OF_MONTH) != lastResetDate.get(Calendar.DAY_OF_MONTH);
    }

    private void resetHighAndLow(double currentTemp) {
        dailyHighTemp = currentTemp; // Reset high to current temperature
        dailyLowTemp = currentTemp;  // Reset low to current temperature
        lastResetTime = System.currentTimeMillis(); // Update last reset time
    }

    private void saveHighAndLow() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("dailyHighTemp", (float) dailyHighTemp);
        editor.putFloat("dailyLowTemp", (float) dailyLowTemp);
        editor.putLong("lastResetTime", lastResetTime);
        editor.apply();
    }

    private void loadHighAndLow() {
        dailyHighTemp = sharedPreferences.getFloat("dailyHighTemp", (float) Double.MIN_VALUE);
        dailyLowTemp = sharedPreferences.getFloat("dailyLowTemp", (float) Double.MAX_VALUE);
        lastResetTime = sharedPreferences.getLong("lastResetTime", 0);

        // If no data is saved, initialize with current temperature
        if (dailyHighTemp == Double.MIN_VALUE || dailyLowTemp == Double.MAX_VALUE) {
            dailyHighTemp = Double.MIN_VALUE;
            dailyLowTemp = Double.MAX_VALUE;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        }
    }

    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("TemperatureUnit")) {
                requestLocation();
            }
        }
    };

    private double getTemperatureInUnit(double tempInCelsius, String tempUnit) {
        if (tempUnit.equals("F")) {
            return (tempInCelsius * 9 / 5) + 32;
        } else {
            return tempInCelsius;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the listener
        SharedPreferences appSettings = getSharedPreferences("AppSetting", MODE_PRIVATE);
        appSettings.registerOnSharedPreferenceChangeListener(sharedPreferenceListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener
        SharedPreferences appSettings = getSharedPreferences("AppSetting", MODE_PRIVATE);
        appSettings.unregisterOnSharedPreferenceChangeListener(sharedPreferenceListener);
    }

}