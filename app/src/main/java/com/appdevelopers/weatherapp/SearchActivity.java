package com.appdevelopers.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appdevelopers.weatherapp.Adapter.SearchAdapter;
import com.appdevelopers.weatherapp.Model.GeoLocation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {
    private SearchView searchView;
    private SearchAdapter adapter;
    private List<String> locationList;
    private OpenWeatherMapService apiService;
    private final String API_KEY = "151be366b244fa29f3132ffb1b84453d";
    private TextView noLocationFound;
    private RecyclerView searchRecycleView;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        noLocationFound = findViewById(R.id.noLocationFound);
        noLocationFound.setVisibility(View.GONE);
        searchRecycleView = findViewById(R.id.searchRecyclerView);
        searchView = findViewById(R.id.searchView);

        // Set Layout Manager for RecyclerView
        searchRecycleView.setLayoutManager(new LinearLayoutManager(this));
        locationList = new ArrayList<>();
        adapter = new SearchAdapter(locationList, this::openWeatherActivity);
        searchRecycleView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(OpenWeatherMapService.class);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchLocations(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2) { // Start searching after 3 characters
                    if (searchRunnable != null) handler.removeCallbacks(searchRunnable);
                    searchRunnable = () -> fetchLocations(newText);
                    handler.postDelayed(searchRunnable, 500); // Debounce to prevent excessive API calls
                }
                return false;
            }
        });

    }

    private void fetchLocations(String query) {
        apiService.getLocations(query, 10, API_KEY).enqueue(new Callback<List<GeoLocation>>() {
            @Override
            public void onResponse(Call<List<GeoLocation>> call, Response<List<GeoLocation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    locationList.clear();
                    for (GeoLocation location : response.body()) {
                        String name = location.getName();
                        String country = location.getCountry();
                        String state = location.getState();
                        String formattedLocation = name + (state != null && !state.isEmpty() ? ", " + state : "") + ", " + country;
                        locationList.add(formattedLocation);
                    }
                    adapter.updateList(locationList);
                    noLocationFound.setVisibility(locationList.isEmpty() ? View.VISIBLE : View.GONE);
                } else {
                    Log.e("SearchActivity", "Error: No data received");
                }
            }

            @Override
            public void onFailure(Call<List<GeoLocation>> call, Throwable t) {
                Log.e("SearchActivity", "Error fetching locations: " + t.getMessage());
            }
        });
    }

    private void openWeatherActivity(String cityName) {
        // Start MainActivity and pass the city name
        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        intent.putExtra("CITY_NAME", cityName);
        startActivity(intent);
    }
}
