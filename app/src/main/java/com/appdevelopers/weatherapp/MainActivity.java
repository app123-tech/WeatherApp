package com.appdevelopers.weatherapp;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.appwidget.AppWidgetManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.airbnb.lottie.L;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Response;
import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Retrofit;

public class MainActivity extends BaseActivity {
    private ActivityResultLauncher<IntentSenderRequest> activityResultLauncher;
    private boolean isUpdateRequired = false;
    private static final String API_KEY = "151be366b244fa29f3132ffb1b84453d";
    private static final String PREFS_NAME = "WeatherAppPrefs";
    private static final String KEY_CITY = "City";
    private static final String KEY_HIGH_TEMP = "HighTemp";
    private static final String KEY_LOW_TEMP = "LowTemp";
    private static final String KEY_LAST_UPDATED = "LastUpdated";
    private String lastCity = "";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private ImageView imageViewSearch, imageViewSearch2, imageViewSideNavigationBar, imageViewHourlyForecastNow, imageViewHourlyForecast2, imageViewHourlyForecast3, imageViewHourlyForecast4, imageViewHourlyForecast5, imageViewHourlyForecast6, imageViewHourlyForecast7, imageViewHourlyForecast8, imageViewHourlyForecast9, imageViewHourlyForecast10;
    private ImageView imageViewSetting, imageViewMenuClose, imageViewDayForecastToday, imageViewNightForecastToday, imageViewDayForecastFirst, imageViewNightForecastFirst, imageViewDayForecastSecond, imageViewNightForecastSecond, imageViewDayForecastThird, imageViewNightForecastThird, imageViewDayForecastFourth, imageViewNightForecastFourth, imageViewDayForecastFifth, imageViewNightForecastFifth, imageViewWindArrow;
    private TextView textViewActualValue, textViewFeelsLikeValue, textViewRateOurApp, textViewShareOurApp, textViewAboutApp, textViewTermsOfUse, textViewPrivacyPolicy, textViewMaxTemperatureToday, textViewMinTemperatureToday, textViewMaxTemperatureFirst, textViewMinTemperatureFirst, textViewMaxTemperatureSecond, textViewMinTemperatureSecond, textViewMaxTemperatureThird, textViewMinTemperatureThird, textViewMaxTemperatureFourth, textViewMinTemperatureFourth, textViewMinTemperatureFifth, textViewMaxTemperatureFifth, textViewWeatherDayToday, textViewWeatherDayFirst, textViewWeatherDaySecond, textViewWeatherDayThird, textViewWeatherDayFourth, textViewWeatherDayFifth;
    private TextView textViewCityName, textViewFeelsLike, textViewHighTemp, textViewLowTemp, textViewTemp, textViewHourlyForecast, textViewHourlyForecast2, textViewHourlyForecast3, textViewHourlyForecast4, textViewHourlyForecast5, textViewHourlyForecast6, textViewHourlyForecast7, textViewHourlyForecast8, textViewHourlyForecast9, textViewHourlyForecast10, textViewTempNow, textViewTemp2, textViewTemp3, textViewTemp4, textViewTemp5, textViewTemp6, textViewTemp7, textViewTemp8, textViewTemp9, textViewTemp10, textViewSunriseTime, textViewSunsetTime, textViewWindSpeed, textViewAQIValue, textViewWindDirectionSpeed, textViewHumidityValue, textViewHumidityContent, textViewWindSpeedCenter, textViewN, textViewS, textViewW, textViewE;
    private SharedPreferences sharedPreferences;
    private String selectedCity = "Kathmandu";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double dailyHighTemp = Double.MIN_VALUE; // Track the highest temperature of the day
    private double dailyLowTemp = Double.MAX_VALUE;  // Track the lowest temperature of the day
    private long lastResetTime = 0; // Track the last time high and low were reset
    private boolean isLanguageChange = false;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private int getWeatherIcon(String description) {
        String lowerCaseDescription = description.toLowerCase();
        if (lowerCaseDescription.contains("thunderstorm")) {
            return R.drawable.thunderstorm; // Icon for all thunderstorm conditions
        } else if (lowerCaseDescription.contains("drizzle")) {
            return R.drawable.drizzle; // Icon for all drizzle conditions
        } else if (lowerCaseDescription.contains("rain")) {
            if (lowerCaseDescription.contains("freezing rain")) {
                return R.drawable.freezing_rain; // Icon for freezing rain
            } else if (lowerCaseDescription.contains("shower rain")) {
                return R.drawable.shower_rain; // Icon for shower rain
            } else {
                return R.drawable.rain; // Icon for general rain
            }
        } else if (lowerCaseDescription.contains("snow")) {
            if (lowerCaseDescription.contains("sleet")) {
                return R.drawable.sleet; // Icon for sleet
            } else if (lowerCaseDescription.contains("shower snow")) {
                return R.drawable.shower_snow; // Icon for shower snow
            } else {
                return R.drawable.snow; // Icon for general snow
            }
        } else if (lowerCaseDescription.contains("mist") || lowerCaseDescription.contains("fog") || lowerCaseDescription.contains("haze")) {
            return R.drawable.mist; // Icon for mist, fog, and haze
        } else if (lowerCaseDescription.contains("clear sky")) {
            return R.drawable.clear_sky; // Icon for clear sky
        } else if (lowerCaseDescription.contains("clouds")) {
            if (lowerCaseDescription.contains("few clouds")) {
                return R.drawable.few_clouds; // Icon for few clouds
            } else if (lowerCaseDescription.contains("scattered clouds")) {
                return R.drawable.scattered_clouds; // Icon for scattered clouds
            } else if (lowerCaseDescription.contains("broken clouds")) {
                return R.drawable.broken_clouds; // Icon for broken clouds
            } else if (lowerCaseDescription.contains("overcast clouds")) {
                return R.drawable.overcast_clouds; // Icon for overcast clouds
            } else {
                return R.drawable.cloud; // Default icon for other cloud conditions
            }
        } else if (lowerCaseDescription.contains("smoke") || lowerCaseDescription.contains("sand") ||
                lowerCaseDescription.contains("dust") || lowerCaseDescription.contains("volcanic ash")) {
            return R.drawable.weather2; // Icon for smoke, sand, dust, and volcanic ash
        } else if (lowerCaseDescription.contains("squalls") || lowerCaseDescription.contains("tornado")) {
            return R.drawable.extreme; // Icon for squalls and tornado
        } else {
            return R.drawable.resource_default; // Default icon for unknown weather conditions
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), results -> {
            if (results.getResultCode() == RESULT_OK) {
                Log.d(TAG, "Update flow completed successfully!");
                Toast.makeText(this, "App updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "Update flow failed! Result code: " + results.getResultCode());
                Toast.makeText(this, "App update failed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        CheckForAppUpdate();

        if (!NetworkUtils.isConnected(this)) {
            Intent intent = new Intent(MainActivity.this, NoInternetActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        initializeViews();
        imageViewSideNavigationBar.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            int id = menuItem.getItemId();
            if (id == R.id.setting) {
                startActivity(new Intent(MainActivity.this, Setting.class));
            } else if (id == R.id.share) {
            } else if (id == R.id.language) {
            }
            return true;
        });

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        selectedCity = sharedPreferences.getString(KEY_CITY, "Kathmandu");
        lastCity = selectedCity;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (getIntent().hasExtra("LATITUDE") && getIntent().hasExtra("LONGITUDE") && getIntent().hasExtra("CITY")) {

            double lat = getIntent().getDoubleExtra("LATITUDE", 0);
            double lon = getIntent().getDoubleExtra("LONGITUDE", 0);
            String city = getIntent().getStringExtra("CITY");
            textViewCityName.setText(city);
            fetchWeatherData(lat, lon, city);
        } else {
            requestLocation();
        }
        loadHighAndLow();

        imageViewSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });
    }

    private void initializeViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        imageViewSearch = findViewById(R.id.imageViewSearch);
        imageViewSideNavigationBar = findViewById(R.id.imageViewSideNavigationBar);
        textViewCityName = findViewById(R.id.textViewCityName);
        textViewFeelsLike = findViewById(R.id.textViewFeelsLike);
        textViewHighTemp = findViewById(R.id.textViewHighTemp);
        textViewLowTemp = findViewById(R.id.textViewLowTemp);
        textViewTemp = findViewById(R.id.textViewTemp);
        textViewHourlyForecast = findViewById(R.id.textViewHourlyForecast);
        textViewHourlyForecast2 = findViewById(R.id.textViewHourlyForecast2);
        textViewHourlyForecast3 = findViewById(R.id.textViewHourlyForecast3);
        textViewHourlyForecast4 = findViewById(R.id.textViewHourlyForecast4);
        textViewHourlyForecast5 = findViewById(R.id.textViewHourlyForecast5);
        textViewHourlyForecast6 = findViewById(R.id.textViewHourlyForecast6);
        textViewHourlyForecast7 = findViewById(R.id.textViewHourlyForecast7);
        textViewHourlyForecast8 = findViewById(R.id.textViewHourlyForecast8);
        textViewHourlyForecast9 = findViewById(R.id.textViewHourlyForecast9);
        textViewHourlyForecast10 = findViewById(R.id.textViewHourlyForecast10);
        textViewTempNow = findViewById(R.id.textViewTempNow);
        imageViewSideNavigationBar = findViewById(R.id.imageViewSideNavigationBar);
        textViewTemp2 = findViewById(R.id.textViewTemp2);
        textViewTemp3 = findViewById(R.id.textViewTemp3);
        textViewTemp4 = findViewById(R.id.textViewTemp4);
        textViewTemp5 = findViewById(R.id.textViewTemp5);
        textViewTemp6 = findViewById(R.id.textViewTemp6);
        textViewTemp7 = findViewById(R.id.textViewTemp7);
        textViewTemp8 = findViewById(R.id.textViewTemp8);
        textViewTemp9 = findViewById(R.id.textViewTemp9);
        textViewTemp10 = findViewById(R.id.textViewTemp10);
        textViewSunriseTime = findViewById(R.id.textViewSunriseTime);
        textViewSunsetTime = findViewById(R.id.textViewSunsetTime);
        imageViewHourlyForecastNow = findViewById(R.id.imageViewHourlyForecastNow);
        imageViewHourlyForecast2 = findViewById(R.id.imageViewHourlyForecast2);
        imageViewHourlyForecast3 = findViewById(R.id.imageViewHourlyForecast3);
        imageViewHourlyForecast4 = findViewById(R.id.imageViewHourlyForecast4);
        imageViewHourlyForecast5 = findViewById(R.id.imageViewHourlyForecast5);
        imageViewHourlyForecast6 = findViewById(R.id.imageViewHourlyForecast6);
        imageViewHourlyForecast7 = findViewById(R.id.imageViewHourlyForecast7);
        imageViewHourlyForecast8 = findViewById(R.id.imageViewHourlyForecast8);
        imageViewHourlyForecast9 = findViewById(R.id.imageViewHourlyForecast9);
        imageViewHourlyForecast10 = findViewById(R.id.imageViewHourlyForecast10);
        textViewWindSpeed = findViewById(R.id.textViewWindSpeed);
        textViewAQIValue = findViewById(R.id.textViewAQIValue);
        textViewWindDirectionSpeed = findViewById(R.id.textViewDirectionSpeed);
        textViewHumidityValue = findViewById(R.id.textViewHumidityValue);
        textViewHumidityContent = findViewById(R.id.textViewHumidityContent);

        textViewWeatherDayToday = findViewById(R.id.textViewWeatherDayToday);
        textViewWeatherDayFirst = findViewById(R.id.textViewWeatherDayFirst);
        textViewWeatherDaySecond = findViewById(R.id.textViewWeatherDaySecond);
        textViewWeatherDayThird = findViewById(R.id.textViewWeatherDayThird);
        textViewWeatherDayFourth = findViewById(R.id.textViewWeatherDayFourth);
        textViewWeatherDayFifth = findViewById(R.id.textViewWeatherDayFifth);

        textViewMaxTemperatureToday = findViewById(R.id.textViewMaxTemperatureToday);
        textViewMinTemperatureToday = findViewById(R.id.textViewMinTemperatureToday);
        textViewMaxTemperatureFirst = findViewById(R.id.textViewMaxTemperatureFirst);
        textViewMinTemperatureFirst = findViewById(R.id.textViewMinTemperatureFirst);
        textViewMaxTemperatureSecond = findViewById(R.id.textViewMaxTemperatureSecond);
        textViewMinTemperatureSecond = findViewById(R.id.textViewMinTemperatureSecond);
        textViewMaxTemperatureThird = findViewById(R.id.textViewMaxTemperatureThird);
        textViewMinTemperatureThird = findViewById(R.id.textViewMinTemperatureThird);
        textViewMaxTemperatureFourth = findViewById(R.id.textViewMaxTemperatureFourth);
        textViewMinTemperatureFourth = findViewById(R.id.textViewMinTemperatureFourth);
        textViewMaxTemperatureFifth = findViewById(R.id.textViewMaxTemperatureFifth);
        textViewMinTemperatureFifth = findViewById(R.id.textViewMinTemperatureFifth);
        imageViewDayForecastToday = findViewById(R.id.imageViewDayForecastToday);
        imageViewNightForecastToday = findViewById(R.id.imageViewNightForecastToday);
        imageViewDayForecastFirst = findViewById(R.id.imageViewDayForecastFirst);
        imageViewNightForecastFirst = findViewById(R.id.imageViewNightForecastFirst);
        imageViewDayForecastSecond = findViewById(R.id.imageViewDayForecastSecond);
        imageViewNightForecastSecond = findViewById(R.id.imageViewNightForecastSecond);
        imageViewDayForecastThird = findViewById(R.id.imageViewDayForecastThird);
        imageViewNightForecastThird = findViewById(R.id.imageViewNightForecastThird);
        imageViewDayForecastFourth = findViewById(R.id.imageViewDayForecastFourth);
        imageViewNightForecastFourth = findViewById(R.id.imageViewNightForecastFourth);
        imageViewDayForecastFifth = findViewById(R.id.imageViewDayForecastFifth);
        imageViewNightForecastFifth = findViewById(R.id.imageViewNightForecastFifth);
        imageViewWindArrow = findViewById(R.id.imageViewWindArrow);
        textViewWindSpeedCenter = findViewById(R.id.textViewWindSpeedCenter);
        textViewN = findViewById(R.id.textViewN);
        textViewS = findViewById(R.id.textViewS);
        textViewW = findViewById(R.id.textViewW);
        textViewE = findViewById(R.id.textViewE);
        textViewFeelsLikeValue = findViewById(R.id.textViewFeelsLikeValue);
        textViewActualValue = findViewById(R.id.textViewActualValue);

        View headerView = navigationView.getHeaderView(0);
        imageViewMenuClose = headerView.findViewById(R.id.imageViewMenuClose);
        textViewRateOurApp = headerView.findViewById(R.id.textViewRateOurApp);
        textViewShareOurApp = headerView.findViewById(R.id.textViewShareOurApp);
        textViewAboutApp = headerView.findViewById(R.id.textViewAboutApp);
        textViewTermsOfUse = headerView.findViewById(R.id.textViewTermsOfUse);
        textViewPrivacyPolicy = headerView.findViewById(R.id.textViewPrivacyPolicy);
        imageViewSetting = headerView.findViewById(R.id.imageViewSetting);
        imageViewSearch2 = headerView.findViewById(R.id.imageViewSearch2);

        imageViewSideNavigationBar.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        imageViewMenuClose.setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.START));

        textViewRateOurApp.setOnClickListener(view -> {
            try {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "App not found on Play Store.", Toast.LENGTH_SHORT).show();
            }
        });

        textViewShareOurApp.setOnClickListener(view -> {
            String appPackageName = getPackageName();
            String playStoreLink = "https://play.google.com/store/apps/details?id=" + appPackageName;
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareMessage = "Check out this amazing Weather App: " + playStoreLink;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            try {
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(MainActivity.this, "No application found to share the app.", Toast.LENGTH_SHORT).show();
            }
        });

        imageViewSetting.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Setting.class);
            startActivity(intent);
        });

        imageViewSearch.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        imageViewSearch2.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        textViewAboutApp.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AboutOurApp.class);
            startActivity(intent);
        });
        textViewTermsOfUse.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TermsAndConditions.class);
            startActivity(intent);
        });
        textViewPrivacyPolicy.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PrivacyPolicy.class);
            startActivity(intent);
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void CheckForAppUpdate() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            Log.d(TAG, "Update Available: " + appUpdateInfo.updateAvailability());
            Log.d(TAG, "Is immediate update allowed: " + appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE));
            Log.d(TAG, "Is flexible update allowed: " + appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE));

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    Log.d(TAG, "Immediate update available");
                    Toast.makeText(this, "Immediate update is available. Please update the app", Toast.LENGTH_SHORT).show();
                    isUpdateRequired = true;
                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, activityResultLauncher, AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build());
                    } catch (Exception e) {
                        Log.e(TAG, "Error starting update flow: " + e.getMessage());
                        Toast.makeText(this, "Failed starting update flow", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Log.d(TAG, "Update available but not allowed for immediate update");
                }
            } else {
                Log.d(TAG, "No update available");
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error checking for updates: " + e.getMessage());
        });
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getDeviceLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Location location = task.getResult();
                getCityName(location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(MainActivity.this, "Unable to get location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDeviceLocation() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setNumUpdates(1);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null && !locationResult.getLocations().isEmpty()) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    fetchWeatherData(latitude, longitude, selectedCity);
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                }
            }
        }
    };

    private boolean handleIntentLocationData() {
        if (getIntent().hasExtra("LATITUDE") && getIntent().hasExtra("LONGITUDE") && getIntent().hasExtra("CITY")) {
            double lat = getIntent().getDoubleExtra("LATITUDE", 0);
            double lon = getIntent().getDoubleExtra("LONGITUDE", 0);
            String city = getIntent().getStringExtra("CITY");

            textViewCityName.setText(city);
            fetchWeatherData(lat, lon, city);
            fetchFiveDayForecast(lat, lon);
            return true;
        }
        return false;
    }

    private void fetchFiveDayForecast(double latitude, double longitude) {
        //  Retrofit retrofit = ApiClient.getClient();
        String units = getTemperatureUnit().equals("F") ? "imperial" : "metric";
        OpenWeatherMapService apiService = ApiClient.getClient().create(OpenWeatherMapService.class);
        Call<WeatherResponse> call = apiService.getFiveDayForecast(latitude, longitude, API_KEY, units);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse forecastData = response.body();
                    updateFiveDayForecastUI(forecastData); // Update five-day forecast UI
                } else {
                    Log.e("WeatherApi", "Failed to fetch five-day forecast: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Forecast error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFiveDayForecastUI(WeatherResponse forecastData) {
        if (forecastData == null || forecastData.getList() == null || forecastData.getList().isEmpty()) {
            Log.e("WeatherApi", "No forecast data available");
            return;
        }

        Map<String, List<WeatherResponse.ForecastItem>> dailyForecastMap = new HashMap<>();
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (WeatherResponse.ForecastItem item : forecastData.getList()) {
            String dayKey = sdfDay.format(new Date(item.getDt() * 1000));
            if (!dailyForecastMap.containsKey(dayKey)) {
                dailyForecastMap.put(dayKey, new ArrayList<>());
            }
            dailyForecastMap.get(dayKey).add(item);
        }
        List<String> sortedDays = new ArrayList<>(dailyForecastMap.keySet());
        Collections.sort(sortedDays);

        Calendar today = Calendar.getInstance();
        String todayKey = sdfDay.format(today.getTime());
        if (!sortedDays.contains(todayKey)) {
            sortedDays.add(0, todayKey); // Exclude today from the forecast
        }
        SimpleDateFormat sdfDisplay = new SimpleDateFormat("EEEE", Locale.getDefault());

        for (int i = 0; i < Math.min(6, sortedDays.size()); i++) {
            String dayKey = sortedDays.get(i);
            List<WeatherResponse.ForecastItem> dayItems = dailyForecastMap.get(dayKey);

            if (dayItems == null || dayItems.isEmpty()) continue;
            String description = dayItems.get(0).getWeather().get(0).getDescription();
            int iconResId = getWeatherIcon(description);

            // Calculate max and min temperature for the day
            double maxTemp = -Double.MAX_VALUE;
            double minTemp = Double.MAX_VALUE;
            String iconCode = "";

            for (WeatherResponse.ForecastItem item : dayItems) {
                maxTemp = Math.max(maxTemp, item.getMain().getTemp_max());
                minTemp = Math.min(minTemp, item.getMain().getTemp_min());

                // Use midday weather icon (12 PM to 3 PM)
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date(item.getDt() * 1000));
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                if (hour >= 12 && hour < 15) {
                    iconCode = item.getWeather().get(0).getIcon();
                }
            }

            if (iconCode.isEmpty()) {
                iconCode = dayItems.get(0).getWeather().get(0).getIcon(); // Fallback to first item's icon
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(dayItems.get(0).getDt() * 1000));
            String displayDay = sdfDisplay.format(cal.getTime());


            switch (i) {
                case 0:
                    textViewWeatherDayToday.setText("Today");
                    textViewMaxTemperatureToday.setText(String.format("%d°C", Math.round(maxTemp)));
                    textViewMinTemperatureToday.setText(String.format("%d°C", Math.round(minTemp)));
                    imageViewDayForecastToday.setImageResource(iconResId);
                    break;
                case 1:
                    textViewWeatherDayFirst.setText(displayDay);
                    textViewMaxTemperatureFirst.setText(String.format("%d°C", Math.round(maxTemp)));
                    textViewMinTemperatureFirst.setText(String.format("%d°C", Math.round(minTemp)));
                    imageViewDayForecastFirst.setImageResource(iconResId);
                    break;
                case 2:
                    textViewWeatherDaySecond.setText(displayDay);
                    textViewMaxTemperatureSecond.setText(String.format("%d°C", Math.round(maxTemp)));
                    textViewMinTemperatureSecond.setText(String.format("%d°C", Math.round(minTemp)));
                    imageViewDayForecastSecond.setImageResource(iconResId);
                    break;
                case 3:
                    textViewWeatherDayThird.setText(displayDay);
                    textViewMaxTemperatureThird.setText(String.format("%d°C", Math.round(maxTemp)));
                    textViewMinTemperatureThird.setText(String.format("%d°C", Math.round(minTemp)));
                    imageViewDayForecastThird.setImageResource(iconResId);
                    break;
                case 4:
                    textViewWeatherDayFourth.setText(displayDay);
                    textViewMaxTemperatureFourth.setText(String.format("%d°C", Math.round(maxTemp)));
                    textViewMinTemperatureFourth.setText(String.format("%d°C", Math.round(minTemp)));
                    imageViewDayForecastFourth.setImageResource(iconResId);
                    break;
                case 5:
                    textViewWeatherDayFifth.setText(displayDay);
                    textViewMaxTemperatureFifth.setText(String.format("%d°C", Math.round(maxTemp)));
                    textViewMinTemperatureFifth.setText(String.format("%d°C", Math.round(minTemp)));
                    imageViewDayForecastFifth.setImageResource(iconResId);
                    break;
            }
        }
    }

    private void updateHourlyForecast(WeatherResponse forecast) {
        String tempUnit = getTemperatureUnit();
        if (forecast == null || forecast.getList() == null || forecast.getList().isEmpty()) {
            Toast.makeText(this, "Hourly forecast data not available", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("ha", Locale.getDefault());

        for (int i = 0; i < Math.min(10, forecast.getList().size()); i++) {
            WeatherResponse.ForecastItem forecastItem = forecast.getList().get(i);
            String description = forecastItem.getWeather().get(0).getDescription();
            int iconResId = getWeatherIcon(description);
            String formattedTime;

            try {
                Date date = inputFormat.parse(forecastItem.getDt_txt());
                formattedTime = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                formattedTime = forecastItem.getDt_txt().split(" ")[1];
            }

            double temp = forecastItem.getMain().getTemp();
            if (tempUnit.equals("F")) {
                temp = (temp * 9 / 5) + 32;
            }
            String tempDisplay = String.format("%.1f°%s", temp, tempUnit);
            String iconUrl = "https://openweathermap.org/img/wn/" + forecastItem.getWeather().get(0).getIcon() + ".png";

            switch (i) {
                case 0:
                    textViewTempNow.setText(tempDisplay);
                    textViewHourlyForecast.setText(formattedTime);
                    imageViewHourlyForecastNow.setImageResource(iconResId);
                    break;
                case 1:
                    textViewTemp2.setText(tempDisplay);
                    textViewHourlyForecast2.setText(formattedTime);
                    imageViewHourlyForecast2.setImageResource(iconResId);
                    break;
                case 2:
                    textViewTemp3.setText(tempDisplay);
                    textViewHourlyForecast3.setText(formattedTime);
                    imageViewHourlyForecast3.setImageResource(iconResId);
                    break;
                case 3:
                    textViewTemp4.setText(tempDisplay);
                    textViewHourlyForecast4.setText(formattedTime);
                    imageViewHourlyForecast4.setImageResource(iconResId);
                    break;
                case 4:
                    textViewTemp5.setText(tempDisplay);
                    textViewHourlyForecast5.setText(formattedTime);
                    imageViewHourlyForecast5.setImageResource(iconResId);
                    break;
                case 5:
                    textViewTemp6.setText(tempDisplay);
                    textViewHourlyForecast6.setText(formattedTime);
                    imageViewHourlyForecast6.setImageResource(iconResId);
                    break;
                case 6:
                    textViewTemp7.setText(tempDisplay);
                    textViewHourlyForecast7.setText(formattedTime);
                    imageViewHourlyForecast7.setImageResource(iconResId);
                    break;
                case 7:
                    textViewTemp8.setText(tempDisplay);
                    textViewHourlyForecast8.setText(formattedTime);
                    imageViewHourlyForecast8.setImageResource(iconResId);
                    break;
                case 8:
                    textViewTemp9.setText(tempDisplay);
                    textViewHourlyForecast9.setText(formattedTime);
                    imageViewHourlyForecast9.setImageResource(iconResId);
                    break;
                case 9:
                    textViewTemp10.setText(tempDisplay);
                    textViewHourlyForecast10.setText(formattedTime);
                    imageViewHourlyForecast10.setImageResource(iconResId);
                    break;
            }
        }
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
        String units = getTemperatureUnit().equals("F") ? "imperial" : "metric";
        OpenWeatherMapService apiService = ApiClient.getClient().create(OpenWeatherMapService.class);
        Call<WeatherResponse> call = apiService.getCurrentWeatherByCoords(latitude, longitude, API_KEY, units);
        Call<WeatherResponse> hourlyCall = apiService.getFiveDayForecast(latitude, longitude, API_KEY, units);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherData = response.body();
                    updateUI(weatherData);
                    //updateHourlyForecast(weatherData);
                    fetchFiveDayForecast(latitude, longitude);
                    fetchAQIData(weatherData.getCoord().getLat(), weatherData.getCoord().getLon());
                    List<WeatherResponse.ForecastItem> forecastItems = response.body().getList();
                    Map<String, List<WeatherResponse.ForecastItem>> dailyItemsMap = new HashMap<>();
                    SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
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

        hourlyCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse hourlyData = response.body();
                    updateHourlyForecast(hourlyData);
                } else {
                    Log.e("WeatherApi", "Failed to fetch hourly forecast: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("WeatherApi", "Error fetching hourly forecast: " + t.getMessage());
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
        Call<WeatherResponse> call = apiService.getCurrentWeatherByCity(city, API_KEY, getTemperatureUnit().equals("F") ? "imperial" : "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                    selectedCity = city;
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

    private void fetchAQIData(double lat, double lon) {
        Retrofit retrofit = ApiClient.getClient();
        OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);
        Call<AqiResponse> aqiCall = service.getAirQualityIndex(lat, lon, API_KEY);
        aqiCall.enqueue(new Callback<AqiResponse>() {
            @Override
            public void onResponse(Call<AqiResponse> call, Response<AqiResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().getList().isEmpty()) {
                    int aqi = response.body().getList().get(0).getMain().getAqi();
                    textViewAQIValue.setText(String.valueOf("" + aqi));
                } else {
                    textViewAQIValue.setText("AQI: Data unavailable");
                }
            }

            @Override
            public void onFailure(Call<AqiResponse> call, Throwable t) {
                textViewAQIValue.setText("AQI: Error fetching data");
            }
        });
    }

    private void updateUI(WeatherResponse weatherData) {
        runOnUiThread(() -> {
            Log.d("WeatherApi", "API Response: " + new Gson().toJson(weatherData));
            Log.d("WeatherData", "Temp: " + weatherData.getMain().getTemp());
            Log.d("WeatherData", "Temp Max: " + weatherData.getMain().getTemp_max());
            Log.d("WeatherData", "Temp Min: " + weatherData.getMain().getTemp_min());
            Log.d("WeatherData", "Feels Like: " + weatherData.getMain().getFeels_like());

            if (weatherData == null || weatherData.getMain() == null || weatherData.getWeather() == null || weatherData.getWeather().isEmpty()) {
                Log.e("WeatherApi", "Invalid weather data");
                return;
            }

            if (weatherData.getWeather() != null && !weatherData.getWeather().isEmpty()) {
                String description = weatherData.getWeather().get(0).getDescription();
               // String iconResourceName = getWeatherIcon(description);
                int iconResId = getWeatherIcon(description);
               // imageViewIcon.setImageResource(iconResId);
            }

            String tempUnit = getTemperatureUnit();
            double temp = weatherData.getMain().getTemp();
            double feelsLike = weatherData.getMain().getFeels_like();
            double apiHigh = weatherData.getMain().getTemp_max();
            double apiLow = weatherData.getMain().getTemp_min();

            if (tempUnit.equals("F")) {
                temp = (temp * 9 / 5) + 32;
                feelsLike = (feelsLike * 9 / 5) + 32;
                apiHigh = (apiHigh * 9 / 5) + 32;
                apiLow = (apiLow * 9 / 5) + 32;
            }

            double displayHigh, displayLow;
            if (Math.abs(apiHigh - apiLow) < 0.1) {
                if (!isLanguageChange && (isNewDay() || !selectedCity.equals(lastCity))) {
                    resetHighAndLow(temp);
                    lastCity = selectedCity; // update the lastCity to the new location
                }

                if (temp > dailyHighTemp) {
                    dailyHighTemp = temp;
                }
                if (temp < dailyLowTemp) {
                    dailyLowTemp = temp;
                }
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
            textViewFeelsLikeValue.setText(String.format("%.1f%s", feelsLike, tempUnit.equals("C") ? "°C" : "°F"));
            textViewFeelsLike.setText(weatherData.getWeather().get(0).getDescription());
            textViewActualValue.setText(String.format("%.1f%s", temp, tempUnit.equals("C") ? "°C" : "°F"));

            if (weatherData.getSys() != null) {
                long sunriseTime = weatherData.getSys().getSunrise() * 1000L;
                long sunsetTime = weatherData.getSys().getSunset() * 1000L;

                SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
                String sunrise = timeFormat.format(new Date(sunriseTime));
                String sunset = timeFormat.format(new Date(sunsetTime));

                textViewSunriseTime.setText(sunrise);
                textViewSunsetTime.setText(sunset);
            }
            SharedPreferences appSettings = getSharedPreferences("AppSetting", Context.MODE_PRIVATE);
            String windSpeedUnit = appSettings.getString("WindSpeedUnit", "km/h");
            if (weatherData.getWind() != null) {
                double windSpeed = weatherData.getWind().getSpeed(); // Wind speed in m/s
                //  SharedPreferences appSettings = getSharedPreferences("AppSetting", Context.MODE_PRIVATE);
                // String windSpeedUnit = appSettings.getString("WindSpeedUnit", "km/h");

                // Convert wind speed to the selected unit
                if (windSpeedUnit.equals("km/h")) {
                    windSpeed = windSpeed * 3.6; // Convert m/s to km/h
                } else if (windSpeedUnit.equals("mph")) {
                    windSpeed = windSpeed * 2.23694; // Convert m/s to mph
                }
                textViewWindSpeedCenter.setText(String.format("%.1f %s", windSpeed, windSpeedUnit));
                float windDirection = weatherData.getWind().getDeg(); // Wind direction in degrees
                imageViewWindArrow.setRotation(windDirection); // Rotate the arrow image
            }
            double windSpeed = weatherData.getWind().getSpeed();
            if (windSpeedUnit.equals("m/s")) {
                windSpeed = windSpeed * 3.6f;
            } else {
                windSpeed = windSpeed * 3.6f;
            }

            if (windSpeed == 0) {
                textViewWindSpeed.setText("Clam");
                textViewWindDirectionSpeed.setText("No wind");
            } else {
                textViewWindSpeed.setText(String.format("%.1f %s", windSpeed, windSpeedUnit));
                textViewWindDirectionSpeed.setText(weatherData.getWind().getDeg() + "°" + weatherData.getWind().getDirection());
            }
            textViewTempNow.setText(String.format("%.1f%s", temp, tempUnit));
            textViewHumidityValue.setText(weatherData.getMain().getHumidity() + "%");
            double humidity = weatherData.getMain().getHumidity();
            double dewPoint = calculateDewPoint(weatherData.getMain().getTemp(), humidity);
            if (tempUnit.equals("F")) {
                dewPoint = (dewPoint * 9 / 5) + 32;
            }
            textViewHumidityContent.setText(String.format("The dew point is %.1f%s today", dewPoint, tempUnit));

            SharedPreferences widgetPrefs = getSharedPreferences("WidgetPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = widgetPrefs.edit();
            editor.putString("City", selectedCity);
            editor.putString("Temp", String.format("%.1f%s", temp, tempUnit.equals("C") ? "°C" : "°F"));
            editor.putString("HighTemp", String.format("H: %.1f%s", displayHigh, tempUnit.equals("C") ? "°C" : "°F"));
            editor.putString("LowTemp", String.format("L: %.1f%s", displayLow, tempUnit.equals("C") ? "°C" : "°F"));

            if (weatherData.getWeather() != null && !weatherData.getWeather().isEmpty()) {
                String description = weatherData.getWeather().get(0).getDescription();
                int iconResId = getWeatherIcon(description); // Get the drawable resource ID
                //imageViewIcon.setImageResource(iconResId);
            }
            saveHighAndLow();

            Intent widgetIntent = new Intent(this, WeatherWidget.class);
            widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            int[] widgetIds = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(this, WeatherWidget.class));
            widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
            sendBroadcast(widgetIntent);
        });
    }

    private boolean isNewDay() {
        Calendar today = Calendar.getInstance();
        Calendar lastResetDate = Calendar.getInstance();
        lastResetDate.setTimeInMillis(lastResetTime);

        // Check if the year, month, and day are different
        return today.get(Calendar.YEAR) != lastResetDate.get(Calendar.YEAR) || today.get(Calendar.MONTH) != lastResetDate.get(Calendar.MONTH) || today.get(Calendar.DAY_OF_MONTH) != lastResetDate.get(Calendar.DAY_OF_MONTH);
    }

    private void resetHighAndLow(double currentTemp) {
        dailyHighTemp = currentTemp; // Reset high to current temperature
        dailyLowTemp = currentTemp;  // Reset low to current temperature
        lastResetTime = System.currentTimeMillis(); // Update last reset time
    }

    private double calculateDewPoint(double temperature, double humidity) {
        return temperature - ((100 - humidity) / 5);
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
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getDeviceLocation();
                requestLocation();
            } else {
                Log.e("MainActivity", "Permission Denied");
            }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences appSettings = getSharedPreferences("AppSetting", MODE_PRIVATE);
        appSettings.unregisterOnSharedPreferenceChangeListener(sharedPreferenceListener);
    }
}