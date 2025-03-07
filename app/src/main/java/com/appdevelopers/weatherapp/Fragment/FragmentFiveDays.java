package com.appdevelopers.weatherapp.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import com.google.android.gms.location.LocationRequest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdevelopers.weatherapp.Adapter.FiveDaysWeatherAdapter;
import com.appdevelopers.weatherapp.ApiClient;
import com.appdevelopers.weatherapp.Model.FiveDaysWeatherItemModel;
import com.appdevelopers.weatherapp.OpenWeatherMapService;
import com.appdevelopers.weatherapp.R;
import com.appdevelopers.weatherapp.WeatherResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentFiveDays extends Fragment {
    private RecyclerView recyclerView;
    private FiveDaysWeatherAdapter adapter;
    private List<FiveDaysWeatherItemModel> weatherItemList;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;


    public FragmentFiveDays() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_five_days, container, false);
        recyclerView = view.findViewById(R.id.fiveDaysWeatherRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        weatherItemList = new ArrayList<>();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getDeviceLocation();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
        return view;
    }

    private void getDeviceLocation() {
        // Use the backwards-compatible factory method for LocationRequest
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);           // 10 seconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setNumUpdates(1);               // Only one update

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Request location updates only if permission is granted
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } else {
            // If permissions are not granted, request them
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null && !locationResult.getLocations().isEmpty()) {
                android.location.Location location = locationResult.getLastLocation();
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    fetchWeatherData(latitude, longitude);
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                }
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getDeviceLocation();
            } else {
                Log.e("FragmentFiveDays", "Permission Denied");
            }
        }
    }

    private void fetchWeatherData(double latitude, double longitude) {
        OpenWeatherMapService apiService = ApiClient.getClient().create(OpenWeatherMapService.class);
        String apiKey = "151be366b244fa29f3132ffb1b84453d";
        String units = "metric";

        Call<WeatherResponse> call = apiService.getFiveDayForecast(latitude, longitude, apiKey, units);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<WeatherResponse.ForecastItem> forecastItems = response.body().getList();
                    weatherItemList.clear();

                    Map<String, List<WeatherResponse.ForecastItem>> dailyItemsMap = new HashMap<>();
                    SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                    for (WeatherResponse.ForecastItem item : forecastItems) {
                        Date date = new Date(item.getDt() * 1000);
                        String dayKey = sdfDay.format(date);
                        if (!dailyItemsMap.containsKey(dayKey)) {
                            dailyItemsMap.put(dayKey, new ArrayList<>());
                        }
                        dailyItemsMap.get(dayKey).add(item);
                    }

                    List<String> sortedDays = new ArrayList<>(dailyItemsMap.keySet());
                    Collections.sort(sortedDays);

                    String todayKey = sdfDay.format(new Date());
                    sortedDays.remove(todayKey);

                    for (String dayKey : sortedDays) {
                        List<WeatherResponse.ForecastItem> dayItems = dailyItemsMap.get(dayKey);
                        if (dayItems == null || dayItems.isEmpty()) continue;

                        double maxTemp = -Double.MAX_VALUE;
                        double minTemp = Double.MAX_VALUE;
                        String iconCode = "";
                        Date middayDate = null;

                        for (WeatherResponse.ForecastItem item : dayItems) {
                            maxTemp = Math.max(maxTemp, item.getMain().getTemp_max());
                            minTemp = Math.min(minTemp, item.getMain().getTemp_min());

                            Calendar cal = Calendar.getInstance();
                            cal.setTime(new Date(item.getDt() * 1000));
                            int hour = cal.get(Calendar.HOUR_OF_DAY);
                            if (hour >= 12 && hour < 15) {
                                iconCode = item.getWeather().get(0).getIcon();
                                middayDate = new Date(item.getDt() * 1000);
                            }
                        }

                        if (iconCode.isEmpty()) {
                            iconCode = dayItems.get(0).getWeather().get(0).getIcon();
                        }

                        SimpleDateFormat sdfDisplay = new SimpleDateFormat("EEEE", Locale.getDefault());
                        String displayDay = sdfDisplay.format(new Date(dayItems.get(0).getDt() * 1000));

                        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy MMM dd", Locale.getDefault());
                        String displayDate = sdfDate.format(new Date(dayItems.get(0).getDt() * 1000));

                        FiveDaysWeatherItemModel model = new FiveDaysWeatherItemModel();
                        model.setWeatherDay(displayDay);
                        model.setWeatherDate(displayDate);
                        model.setMaxTemperature(String.format("%.1f°C", maxTemp));
                        model.setMinTemperature(String.format("%.1f°C", minTemp));
                        model.setWeatherTitle(dayItems.get(0).getWeather().get(0).getDescription());
                        model.setWeatherImage("https://openweathermap.org/img/wn/" + iconCode + "@2x.png");

                        weatherItemList.add(model);
                    }

                    adapter = new FiveDaysWeatherAdapter(weatherItemList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("FragmentFiveDays", "Error: " + t.getMessage());
            }
        });
    }
}