package com.appdevelopers.weatherapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appdevelopers.weatherapp.ApiClient;
import com.appdevelopers.weatherapp.OpenWeatherMapService;
import com.appdevelopers.weatherapp.R;
import com.appdevelopers.weatherapp.WeatherResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class FragmentTomorrow extends Fragment {
    private static final String API_KEY = "151be366b244fa29f3132ffb1b84453d";
    private static final String PREFS_NAME = "WeatherAppPrefs";
    private static final String DEFAULT_CITY = "Kathmandu";
    private TextView textViewWindSecond, textViewWindSpeed, textViewAQI, textViewAQISpeed, textViewDirection, textViewDirectionSpeed, textViewHumidityValue, textViewHumidityContent;
    private TextView textViewHourlyForecastNow, textViewHourlyForecast10AM, textViewHourlyForecast11AM, textViewHourlyForecast12PM, textViewHourlyForecast1PM, textViewHourlyForecast2PM;
    private TextView textViewHourlyForecastNowTemp, textViewHourlyForecast10AMTemp, textViewHourlyForecast11AMTemp, textViewHourlyForecast12PMTemp, textViewHourlyForecast1PMTemp, textViewHourlyForecast2PMTemp;
    private TextView textViewSunriseTime, textViewSunsetTime;
    private ImageView imageViewHourlyForecastNow, imageViewHourlyForecast10AM, imageViewHourlyForecast11AM, imageViewHourlyForecast12PM, imageViewHourlyForecast1PM, imageViewHourlyForecast2PM;

    public FragmentTomorrow() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tomorrow, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewWindSecond = view.findViewById(R.id.textViewWindSecond);
        textViewWindSpeed = view.findViewById(R.id.textViewWindSpeed);
        textViewAQI = view.findViewById(R.id.textViewAQI);
        textViewAQISpeed = view.findViewById(R.id.textViewAQISpeed);
        textViewDirection = view.findViewById(R.id.textViewDirection);
        textViewDirectionSpeed = view.findViewById(R.id.textViewDirectionSpeed);
        textViewHumidityValue = view.findViewById(R.id.textViewHumidityValue);
        textViewHumidityContent = view.findViewById(R.id.textViewHumidityContent);

        textViewHourlyForecastNow = view.findViewById(R.id.textViewHourlyForecastNow);
        textViewHourlyForecast10AM = view.findViewById(R.id.textViewHourlyForecast10AM);
        textViewHourlyForecast11AM = view.findViewById(R.id.textViewHourlyForecast11AM);
        textViewHourlyForecast12PM = view.findViewById(R.id.textViewHourlyForecast12PM);
        textViewHourlyForecast1PM = view.findViewById(R.id.textViewHourlyForecast1PM);
        textViewHourlyForecast2PM = view.findViewById(R.id.textViewHourlyForecast2PM);

        textViewHourlyForecastNowTemp = view.findViewById(R.id.textViewHourlyForecastNowTemp);              // 3 hour gap interval
        textViewHourlyForecast10AMTemp = view.findViewById(R.id.textViewHourlyForecast10AMTemp);            // 3 hour gap interval
        textViewHourlyForecast11AMTemp = view.findViewById(R.id.textViewHourlyForecast11AMTemp);            // 3 hour gap interval
        textViewHourlyForecast12PMTemp = view.findViewById(R.id.textViewHourlyForecast12PMTemp);            // 3 hour gap interval
        textViewHourlyForecast1PMTemp = view.findViewById(R.id.textViewHourlyForecast1PMTemp);              // 3 hour gap interval
        textViewHourlyForecast2PMTemp = view.findViewById(R.id.textViewHourlyForecast2PMTemp);              // 3 hour gap interval

        textViewSunriseTime = view.findViewById(R.id.textViewSunriseTime);
        textViewSunsetTime = view.findViewById(R.id.textViewSunsetTime);

        imageViewHourlyForecastNow = view.findViewById(R.id.imageViewHourlyForecastNow);
        imageViewHourlyForecast10AM = view.findViewById(R.id.imageViewHourlyForecast10AM);
        imageViewHourlyForecast11AM = view.findViewById(R.id.imageViewHourlyForecast11AM);
        imageViewHourlyForecast12PM = view.findViewById(R.id.imageViewHourlyForecast12PM);
        imageViewHourlyForecast1PM = view.findViewById(R.id.imageViewHourlyForecast1PM);
        imageViewHourlyForecast2PM = view.findViewById(R.id.imageViewHourlyForecast2PM);

        fetchWeatherData(DEFAULT_CITY);
        fetchWeatherForecast(DEFAULT_CITY);
    }

    private void fetchWeatherData(String city) {
        Retrofit retrofit = ApiClient.getClient();
        OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);

        Call<WeatherResponse> call = service.getCurrentWeatherByCity(city, API_KEY, "metric");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    updateCurrentWeatherUI(weatherResponse);
                } else {
                    Toast.makeText(getContext(), "Failed to retrieve weather data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchWeatherForecast(String city) {
        Retrofit retrofit = ApiClient.getClient();
        OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);

        Call<WeatherResponse> call = service.getCurrentWeatherByCity(city, API_KEY, "metric");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<WeatherResponse.Forecast> forecasts = response.body().getList();
                    updateTomorrowWeatherUI(forecasts);
                } else {
                    Toast.makeText(getContext(), "Failed to retrieve forecast", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCurrentWeatherUI(WeatherResponse weatherResponse) {
        textViewHumidityValue.setText(weatherResponse.getMain().getHumidity() + "%");
        textViewWindSpeed.setText(weatherResponse.getMain().getPressure() + " hPa");
    }

    private void updateTomorrowWeatherUI(List<WeatherResponse.Forecast> forecasts) {
        // Get tomorrow's date in the required format
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        String tomorrowDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());

        for (WeatherResponse.Forecast forecast : forecasts) {
            if (forecast.getDt_txt().contains(tomorrowDate + " 12:00:00")) {  // Get midday forecast

                break;  // Stop loop once found
            }
        }
    }
}