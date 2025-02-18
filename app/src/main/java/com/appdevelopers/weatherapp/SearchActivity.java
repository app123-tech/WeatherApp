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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {
    private SearchView searchView;
    private SearchAdapter adapter;
    private List<GeoLocation> locationList = new ArrayList<>();
    private Map<GeoLocation, String> formattedLocations = new HashMap<>();
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
        // locationList = new ArrayList<>();
        adapter = new SearchAdapter(locationList, formattedLocations, this::openWeatherActivity);
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
        // Clear previous results
        locationList.clear();
        formattedLocations.clear();
        adapter.notifyDataSetChanged();

        apiService.getLocations(query, 10, API_KEY).enqueue(new Callback<List<GeoLocation>>() {
            @Override
            public void onResponse(Call<List<GeoLocation>> call, Response<List<GeoLocation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GeoLocation> newLocations = response.body();
                    // Use a set to avoid duplicates based on the formatted address
                    HashSet<String> seenFormatted = new HashSet<>();

                    for (GeoLocation location : newLocations) {
                        // Process each location (asynchronously)
                        processLocation(location, seenFormatted);
                    }
                    // Toggle view visibility based on results
                    if (locationList.isEmpty()) {
                        noLocationFound.setVisibility(View.VISIBLE);
                        searchRecycleView.setVisibility(View.GONE);
                    } else {
                        noLocationFound.setVisibility(View.GONE);
                        searchRecycleView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("SearchActivity", "Error: No data received");
                    noLocationFound.setVisibility(View.VISIBLE);
                    searchRecycleView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<GeoLocation>> call, Throwable t) {
                Log.e("SearchActivity", "Error fetching locations: " + t.getMessage());
                noLocationFound.setVisibility(View.VISIBLE);
                searchRecycleView.setVisibility(View.GONE);
            }
        });
    }

    private void processLocation(GeoLocation location, HashSet<String> seenFormatted) {
        // Convert the country code to its full country name (e.g., "NP" → "Nepal")
        final String fullCountry = getFullCountryName(location.getCountry());

        // If there is no state information, assume it's a country-level result.
        if (location.getState() == null || location.getState().isEmpty()) {
            String formatted = capitalize(location.getName()) + ", " + fullCountry;
            if (!seenFormatted.contains(formatted)) {
                seenFormatted.add(formatted);
                formattedLocations.put(location, formatted);
                locationList.add(location);
                adapter.notifyDataSetChanged();
            }
            return;
        }

        // Otherwise, use the weather API to determine the primary city for these coordinates.
        apiService.getWeather(location.getLat(), location.getLon(), API_KEY)
                .enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        String formatted;
                        if (response.isSuccessful() && response.body() != null) {
                            String weatherCity = response.body().getName(); // e.g., "Kathmandu"
                            if (location.getName().equalsIgnoreCase(weatherCity)) {
                                // It’s a major city; show "City, Country"
                                formatted = capitalize(location.getName()) + ", " + fullCountry;
                            } else {
                                // It’s a sub-locality; display the full hierarchy
                                // (format the state field if it contains multiple parts)
                                String formattedState = formatState(location.getState());
                                formatted = capitalize(location.getName()) + ", " + formattedState + ", " + fullCountry;
                            }
                        } else {
                            // Fallback: use the geocoding state data
                            String formattedState = formatState(location.getState());
                            formatted = capitalize(location.getName()) + ", " + formattedState + ", " + fullCountry;
                        }
                        // Avoid duplicates based on the formatted address string
                        if (!seenFormatted.contains(formatted)) {
                            seenFormatted.add(formatted);
                            formattedLocations.put(location, formatted);
                            locationList.add(location);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        String formattedState = formatState(location.getState());
                        String formatted = capitalize(location.getName()) + ", " + formattedState + ", " + fullCountry;
                        if (!seenFormatted.contains(formatted)) {
                            seenFormatted.add(formatted);
                            formattedLocations.put(location, formatted);
                            locationList.add(location);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private String getFullCountryName(String countryCode) {
        return new Locale("", countryCode).getDisplayCountry(Locale.ENGLISH);
    }

    private String formatState(String state) {
        if (state == null || state.isEmpty()) {
            return "";
        }
        String[] parts = state.split(",");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            sb.append(capitalize(part.trim())).append(", ");
        }
        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2); // Remove trailing comma and space.
        }
        return sb.toString();
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return "";
        String[] words = str.split(" ");
        StringBuilder capitalized = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                capitalized.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return capitalized.toString().trim();
    }

    private void openWeatherActivity(GeoLocation location) {
        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        intent.putExtra("LATITUDE", location.getLat());
        intent.putExtra("LONGITUDE", location.getLon());
        intent.putExtra("CITY", location.getName());
        startActivity(intent);
    }
}
