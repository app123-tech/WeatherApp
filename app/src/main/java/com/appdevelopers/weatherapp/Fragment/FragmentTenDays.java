package com.appdevelopers.weatherapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdevelopers.weatherapp.Adapter.tenDaysWeatherAdapter;
import com.appdevelopers.weatherapp.ApiClient;
import com.appdevelopers.weatherapp.Model.tenDaysWeatherItemModel;
import com.appdevelopers.weatherapp.OpenWeatherMapService;
import com.appdevelopers.weatherapp.R;
import com.appdevelopers.weatherapp.WeatherResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentTenDays extends Fragment {
    private RecyclerView recyclerView;
    private tenDaysWeatherAdapter adapter;
    private List<tenDaysWeatherItemModel> itemModels;

    public FragmentTenDays() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ten_days, container, false);
        recyclerView = view.findViewById(R.id.tenDaysWeatherRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        itemModels = new ArrayList<>();
        fetchWeatherData();
        return view;
    }

    private void fetchWeatherData() {
        OpenWeatherMapService service = ApiClient.getClient().create(OpenWeatherMapService.class);
        String apiKey = "151be366b244fa29f3132ffb1b84453d"; // Replace with your actual API key
        String units = "metric"; // Change to "imperial" for Fahrenheit, "metric" for Celsius

        // Replace with a valid city or coordinates for testing
        String city = "London"; // Replace with actual city name

        service.getForecastByCity(city, apiKey, units).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    if (weatherResponse != null && weatherResponse.getHourly() != null) {
                        // Clear the list before adding new data
                        itemModels.clear();

                        // Process the 5-day forecast
                        List<WeatherResponse.Hourly> hourlyData = weatherResponse.getHourly();
                        for (int i = 0; i < 5; i++) { // Loop over the first 5 entries (3-hour intervals)
                            WeatherResponse.Hourly hourly = hourlyData.get(i * 8); // Every 8th entry represents a day
                            String day = hourly.getDt_txt().split(" ")[0]; // Extract the date (e.g., 2025-02-15)
                            String description = hourly.getWeather().get(0).getDescription();
                            String maxTemp = String.valueOf(hourly.getMain().getTemp_max());
                            String minTemp = String.valueOf(hourly.getMain().getTemp_min());
                            String iconCode = hourly.getWeather().get(0).getIcon();

                            // Construct the URL for the weather icon
                            String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

                            itemModels.add(new tenDaysWeatherItemModel(day, description, maxTemp, minTemp, iconUrl));
                        }

                        // Notify the adapter to update the UI
                        if (adapter == null) {
                            adapter = new tenDaysWeatherAdapter(itemModels);
                            recyclerView.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // Handle failure (e.g., no internet, invalid API key, etc.)
            }
        });
    }
}