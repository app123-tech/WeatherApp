package com.appdevelopers.weatherapp.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationRequest;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentFiveDays extends Fragment {
//    private RecyclerView recyclerView;
//    private FiveDaysWeatherAdapter adapter;
//    private List<FiveDaysWeatherItemModel> weatherItemList;

    public FragmentFiveDays() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ten_days, container, false);
//        recyclerView = view.findViewById(R.id.tenDaysWeatherRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        weatherItemList = new ArrayList<>();
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
//
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
//                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            getDeviceLocation();
//        } else {
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                    LOCATION_PERMISSION_REQUEST_CODE);
//        }
        return view;
//    }
//
//    private void getDeviceLocation() {
//        // Use LocationRequest.Builder for newer versions of play-services-location
//        LocationRequest locationRequest = new LocationRequest.Builder(10000)  // Set interval in milliseconds (10 seconds)
//                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)  // Set priority for high accuracy
//                .setMaxUpdates(1)  // Only get one update
//                .build();
//
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
//                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//            // Request location updates only if permission is granted
//            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
//        } else {
//            // If permissions are not granted, request them
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                    LOCATION_PERMISSION_REQUEST_CODE);
//        }
//    }
//
//    private LocationCallback locationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(@NonNull LocationResult locationResult) {
//            super.onLocationResult(locationResult);
//            if (locationResult != null && locationResult.getLocations().size() > 0) {
//                android.location.Location location = locationResult.getLastLocation();
//                if (location != null) {
//                    double latitude = location.getLatitude();
//                    double longitude = location.getLongitude();
//                    fetchWeatherData(latitude, longitude);
//                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
//                }
//            }
//        }
//    };
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permission, grantResults);
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getDeviceLocation();
//            } else {
//                Log.e("FragmentFiveDays", "Permission Denied");
//            }
//        }
//    }
//
//    private void fetchWeatherData(double latitude, double longitude) {
//        OpenWeatherMapService apiService = ApiClient.getClient().create(OpenWeatherMapService.class);
//        String apiKey = "151be366b244fa29f3132ffb1b84453d";  // Your API key
//        String units = "metric";  // Use metric units
//
//        // Making the API call using latitude and longitude obtained dynamically
//        Call<WeatherResponse> call = apiService.getFiveDayForecastByCity(latitude, longitude, apiKey, units);
//
//        call.enqueue(new Callback<WeatherResponse>() {
//            @Override
//            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    // Handle the response and update the weather data in the RecyclerView
//                    List<WeatherResponse.FiveDaysWeatherItemModel> weatherItems = response.body().getList(); // Assuming 'getList' gives the forecast
//                    weatherItemList.clear();
//                    weatherItemList.addAll(weatherItems);
//                    adapter = new FiveDaysWeatherAdapter(weatherItemList);
//                    recyclerView.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<WeatherResponse> call, Throwable t) {
//                Log.e("FragmentFiveDays", "Weather API request failed: " + t.getMessage());
//            }
//        });
    }
}