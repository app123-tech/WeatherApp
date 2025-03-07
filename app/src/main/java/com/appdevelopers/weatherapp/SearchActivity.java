package com.appdevelopers.weatherapp;

import android.content.Intent;
import android.location.Location;
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
import com.appdevelopers.weatherapp.Model.NominatimLocation;

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
        locationList = new ArrayList<>();
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

        // Use Nominatim to search for locations
        //String nominatimUrl = "https://nominatim.openstreetmap.org/search?q=" + query + "&format=json&addressdetails=1&limit=10";

        // Make a network request to Nominatim (you can use Retrofit or HttpURLConnection)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://nominatim.openstreetmap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NominatimService nominatimService = retrofit.create(NominatimService.class);

        nominatimService.getLocations(query).enqueue(new Callback<List<NominatimLocation>>() {
            @Override
            public void onResponse(Call<List<NominatimLocation>> call, Response<List<NominatimLocation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NominatimLocation> newLocations = response.body();
                    HashSet<String> seenFormatted = new HashSet<>();

                    for (NominatimLocation nominatimLocation : newLocations) {
                        processLocation(nominatimLocation, seenFormatted);
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
                    Log.e("SearchActivity", "Error: No data received or empty response");
                    noLocationFound.setVisibility(View.VISIBLE);
                    searchRecycleView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<NominatimLocation>> call, Throwable t) {
                Log.e("SearchActivity", "Error fetching locations: " + t.getMessage());
                noLocationFound.setVisibility(View.VISIBLE);
                searchRecycleView.setVisibility(View.GONE);
            }
        });
    }

    private void processLocation(NominatimLocation nominatimLocation, HashSet<String> seenFormatted) {
        // Create formatted location name
        String formatted = nominatimLocation.getDisplayName();

        // Avoid duplicates based on the formatted address string
        if (!seenFormatted.contains(formatted)) {
            seenFormatted.add(formatted);
            String country = "";
            String state = "";
            if (nominatimLocation.getAddress() != null) {
                Map<String, String> address = nominatimLocation.getAddress();
                country = address.getOrDefault("country", "");
                state = address.getOrDefault("state", "");
            }

            GeoLocation geoLocation = new GeoLocation(nominatimLocation.getLat(), nominatimLocation.getLon(), formatted, country, state);
            formattedLocations.put(geoLocation, formatted);
            locationList.add(geoLocation);
            adapter.notifyDataSetChanged();

            // Fetch weather data for the selected location using OpenWeatherMap
            fetchWeather(geoLocation);
        }
    }

    private void fetchWeather(GeoLocation location) {
        // OpenWeatherMap API URL
        apiService.getWeather(location.getLat(), location.getLon(), API_KEY).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weather = response.body();
                    // Use the weather data as needed (for example, update the UI)
                    Log.d("Weather", "Weather for " + location.getName() + ": " +
                            weather.getMain().getTemp());
                } else {
                    Log.e("Weather", "Error fetching weather data");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("Weather", "Error fetching weather data: " + t.getMessage());
            }
        });
    }

    private void openWeatherActivity(GeoLocation location) {
        // Retrieve the full formatted string from the map if available.
        String fullFormatted = formattedLocations.get(location);
        String shortName;

        if (fullFormatted != null) {
            // Split the full formatted string by commas.
            String[] parts = fullFormatted.split(",");
            // If there are at least two parts, use them for the short name.
            if (parts.length >= 2) {
                shortName = parts[0].trim() + ", " + parts[1].trim();
            } else {
                shortName = fullFormatted;
            }
        } else {
            // Fallback: if no formatted string is available, use state if present, otherwise country.
            if (location.getState() != null && !location.getState().isEmpty()) {
                shortName = location.getName() + ", " + location.getState();
            } else {
                shortName = location.getName() + ", " + location.getCountry();
            }
        }

        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        intent.putExtra("LATITUDE", location.getLat());
        intent.putExtra("LONGITUDE", location.getLon());
        intent.putExtra("CITY", shortName); // Pass the computed short name
        startActivity(intent);
    }
}
