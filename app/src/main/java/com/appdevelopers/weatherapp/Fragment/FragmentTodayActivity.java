package com.appdevelopers.weatherapp.Fragment;

import android.content.SharedPreferences;
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
import com.appdevelopers.weatherapp.AqiResponse;
import com.appdevelopers.weatherapp.OpenWeatherMapService;
import com.appdevelopers.weatherapp.R;
import com.appdevelopers.weatherapp.WeatherResponse;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentTodayActivity extends Fragment {
    private static final String API_KEY = "151be366b244fa29f3132ffb1b84453d";
    private static final String PREFS_NAME = "WeatherAppPrefs";
    private static final String DEFAULT_CITY = "Kathmandu";
    private TextView textViewWind, textViewWindSecond, textViewWindSpeed, textViewAQI, textViewAQIValue, textViewDirection, textViewDirectionSpeed, textViewHumidity, textViewHumidityValue, textViewHumidityContent;
    private TextView textView24HourlyForecast, textViewHourlyForecast, textViewHourlyForecast2, textViewHourlyForecast3, textViewHourlyForecast4, textViewHourlyForecast5, textViewHourlyForecast6, textViewTempNow, textViewTemp2, textViewTemp3, textViewTemp4, textViewTemp5, textViewTemp6;
    private TextView textViewSunriseTime, textViewSunsetTime;
    private ImageView imageViewHourlyForecastNow, imageViewHourlyForecast2, imageViewHourlyForecast3, imageViewHourlyForecast4, imageViewHourlyForecast5, imageViewHourlyForecast6;
    private SharedPreferences sharedPreferences;

    public FragmentTodayActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today_activity, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initializing TextViews
        textViewWind = view.findViewById(R.id.textViewWind);        // not necessary
        textViewWindSecond = view.findViewById(R.id.textViewWindSecond);
        textViewWindSpeed = view.findViewById(R.id.textViewWindSpeed);
        textViewAQI = view.findViewById(R.id.textViewAQI);
        textViewAQIValue = view.findViewById(R.id.textViewAQIValue);
        textViewDirection = view.findViewById(R.id.textViewDirection);
        textViewDirectionSpeed = view.findViewById(R.id.textViewDirectionSpeed);
        textViewHumidity = view.findViewById(R.id.textViewHumidity);           // not necessary
        textViewHumidityValue = view.findViewById(R.id.textViewHumidityValue);
        textViewHumidityContent = view.findViewById(R.id.textViewHumidityContent);

        textView24HourlyForecast = view.findViewById(R.id.textView24HourlyForecast);            // 3 hrs interval
        textViewHourlyForecast = view.findViewById(R.id.textViewHourlyForecast);      // 3 hrs interval
        textViewHourlyForecast2 = view.findViewById(R.id.textViewHourlyForecast2);    // 3 hrs interval
        textViewHourlyForecast3 = view.findViewById(R.id.textViewHourlyForecast3);    // 3 hrs interval
        textViewHourlyForecast4 = view.findViewById(R.id.textViewHourlyForecast4);    // 3 hrs interval
        textViewHourlyForecast5 = view.findViewById(R.id.textViewHourlyForecast5);      // 3 hrs interval
        textViewHourlyForecast6 = view.findViewById(R.id.textViewHourlyForecast6);      // 3 hrs interval

        textViewTempNow = view.findViewById(R.id.textViewTempNow);          // 3 hrs interval forecast temp
        textViewTemp2 = view.findViewById(R.id.textViewTemp2);              // 3 hrs interval forecast temp
        textViewTemp3 = view.findViewById(R.id.textViewTemp3);              // 3 hrs interval forecast temp
        textViewTemp4 = view.findViewById(R.id.textViewTemp4);              // 3 hrs interval forecast temp
        textViewTemp5 = view.findViewById(R.id.textViewTemp5);              // 3 hrs interval forecast temp
        textViewTemp6 = view.findViewById(R.id.textViewTemp6);              // 3 hrs interval forecast temp

        textViewSunriseTime = view.findViewById(R.id.textViewSunriseTime);
        textViewSunsetTime = view.findViewById(R.id.textViewSunsetTime);

        // Initializing ImageViews
        imageViewHourlyForecastNow = view.findViewById(R.id.imageViewHourlyForecastNow);     // hourly forecast image 3hrs interval
        imageViewHourlyForecast2 = view.findViewById(R.id.imageViewHourlyForecast2);        // hourly forecast image 3hrs interval
        imageViewHourlyForecast3 = view.findViewById(R.id.imageViewHourlyForecast3);        // hourly forecast image 3hrs interval
        imageViewHourlyForecast4 = view.findViewById(R.id.imageViewHourlyForecast4);        // hourly forecast image 3hrs interval
        imageViewHourlyForecast5 = view.findViewById(R.id.imageViewHourlyForecast5);        // hourly forecast image 3hrs interval
        imageViewHourlyForecast6 = view.findViewById(R.id.imageViewHourlyForecast6);        // hourly forecast image 3hrs interval

        fetchWeatherData(DEFAULT_CITY);
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
                    updateUI(weatherResponse);
                    fetchAqiData(weatherResponse);
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

    private void fetchAqiData(WeatherResponse weatherResponse) {
        Retrofit retrofit = ApiClient.getClient();
        OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);

        Call<AqiResponse> aqiCall = service.getAirQualityIndex(weatherResponse.getWind().getDeg(), weatherResponse.getWind().getSpeed(), API_KEY);
        aqiCall.enqueue(new Callback<AqiResponse>() {
            @Override
            public void onResponse(@NonNull Call<AqiResponse> call, @NonNull Response<AqiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AqiResponse aqiResponse = response.body();
                    int aqi = aqiResponse.getList().get(0).getMain().getAqi();
                    textViewAQIValue.setText("AQI: " + aqi);
                } else {
                    textViewAQIValue.setText("AQI: Data not available");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AqiResponse> call, @NonNull Throwable t) {
                textViewAQIValue.setText("AQI: Error fetching data");
            }
        });
    }

    private void updateUI(WeatherResponse weatherResponse) {
        if (weatherResponse == null || weatherResponse.getMain() == null || weatherResponse.getWind() == null) {
            Toast.makeText(getContext(), "Weather data unavailable", Toast.LENGTH_SHORT).show();
            return;
        }

        textViewTempNow.setText(weatherResponse.getMain().getTemp() + "°C");
        textViewHumidityValue.setText(weatherResponse.getMain().getHumidity() + "%");
        textViewWindSpeed.setText(weatherResponse.getWind().getSpeed() * 3.6 + " km/h");
        textViewDirectionSpeed.setText(weatherResponse.getWind().getDeg() + "° " + weatherResponse.getWind().getDirection());

        double temperature = weatherResponse.getMain().getTemp();
        double humidity = weatherResponse.getMain().getHumidity();
        double dewPoint = calculateDewPoint(temperature, humidity);
        textViewHumidityContent.setText("The dew point is " + Math.round(dewPoint) + "° tomorrow");

        // Check if hourly data is available
        if (weatherResponse.getHourly() != null && !weatherResponse.getHourly().isEmpty()) {
            for (int i = 0; i < Math.min(6, weatherResponse.getHourly().size()); i++) {
                WeatherResponse.Hourly hourly = weatherResponse.getHourly().get(i);

                if (hourly != null && hourly.getMain() != null && hourly.getWeather() != null && !hourly.getWeather().isEmpty()) {
                    // Set temperature text for the hourly forecast
                    TextView tempTextView = getHourlyTextView(i);
                    tempTextView.setText(hourly.getMain().getTemp() + "°C");

                    // Set the forecast time text (e.g., "3 PM")
                    TextView timeTextView = getHourlyTimeTextView(i);
                    timeTextView.setText(hourly.getDt_txt());

                    // Set the weather icon and description
                    ImageView imageView = getHourlyImageView(i);
                    String iconUrl = "https://openweathermap.org/img/wn/" + hourly.getWeather().get(0).getIcon() + ".png";
                    Glide.with(getContext()).load(iconUrl).into(imageView);
                }
            }
        } else {
            Toast.makeText(getContext(), "Hourly forecast data not available", Toast.LENGTH_SHORT).show();
        }

        // Handle sunrise and sunset times
        if (weatherResponse.getSys() != null) {
            long sunriseTime = weatherResponse.getSys().getSunrise();
            long sunsetTime = weatherResponse.getSys().getSunset();
            String sunrise = new java.text.SimpleDateFormat("hh:mm a").format(new java.util.Date(sunriseTime * 1000));
            String sunset = new java.text.SimpleDateFormat("hh:mm a").format(new java.util.Date(sunsetTime * 1000));
            textViewSunriseTime.setText(sunrise);
            textViewSunsetTime.setText(sunset);
        }
    }

    private double calculateDewPoint(double temperature, double humidity) {
        return temperature - ((100 - humidity) / 5);
    }

    private TextView getHourlyTextView(int index) {
        switch (index) {
            case 1: return textViewTemp2;
            case 2: return textViewTemp3;
            case 3: return textViewTemp4;
            case 4: return textViewTemp5;
            case 5: return textViewTemp6;
            default: return textViewTempNow;
        }
    }

    private TextView getHourlyTimeTextView(int index) {
        switch (index) {
            case 1: return textViewHourlyForecast2;
            case 2: return textViewHourlyForecast3;
            case 3: return textViewHourlyForecast4;
            case 4: return textViewHourlyForecast5;
            case 5: return textViewHourlyForecast6;
            default: return textViewHourlyForecast;
        }
    }

    private ImageView getHourlyImageView(int index) {
        switch (index) {
            case 1:
                return imageViewHourlyForecast2;
            case 2:
                return imageViewHourlyForecast3;
            case 3:
                return imageViewHourlyForecast4;
            case 4:
                return imageViewHourlyForecast5;
            case 5:
                return imageViewHourlyForecast6;
            default:
                return imageViewHourlyForecastNow;
        }
    }
}