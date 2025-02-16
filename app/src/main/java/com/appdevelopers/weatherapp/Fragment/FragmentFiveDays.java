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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


public class FragmentFiveDays extends Fragment {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private RecyclerView recyclerView;
    private FiveDaysWeatherAdapter adapter;
    private List<FiveDaysWeatherItemModel> itemModels;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;

    public FragmentFiveDays() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ten_days, container, false);
        recyclerView = view.findViewById(R.id.tenDaysWeatherRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        itemModels = new ArrayList<>();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastenInterval(2000);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getDeviceLocation();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        return view;
    }

    private void getDeviceLocation(){
      fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null && locationResult.getLocations().size() > 0) {
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
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getDeviceLocation();
            } else {
                Log.e("FragmentTenDays", "Permission Denied");
            }
        }
    }

    private void fetchWeatherData(double latitude, double longitude) {
OpenWeatherMapService apiService = ApiClient.getClient().create(OpenWeatherMapService.class);
        String apiKey= "151be366b244fa29f3132ffb1b84453d";
        String units = "metric";

        Call<WeatherResponse> call = apiService.getFiveDayForecastByCity(latitude, longitude, apiKey, units);


    }
}