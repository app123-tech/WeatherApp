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

        textView24HourlyForecast = view.findViewById(R.id.textView24HourlyForecast);
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
                    fetchAqiData(weatherResponse.getCoord().getLat(), weatherResponse.getCoord().getLon());
                    fetchFiveDayForecast(city);
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

    private void fetchFiveDayForecast(String city) {
        Retrofit retrofit = ApiClient.getClient();
        OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);

        Call<WeatherResponse> forecastCall = service.getFiveDayForecastByCity(city, API_KEY, "metric");
        forecastCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse forecast = response.body();
                    updateHourlyForecast(forecast);
                } else {
                    Toast.makeText(getContext(), "Failed to retrieve forecast", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Forecast error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAqiData(double lat, double lon) {
        Retrofit retrofit = ApiClient.getClient();
        OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);

        Call<AqiResponse> aqiCall = service.getAirQualityIndex(lat, lon, API_KEY);
        aqiCall.enqueue(new Callback<AqiResponse>() {
            @Override
            public void onResponse(@NonNull Call<AqiResponse> call, @NonNull Response<AqiResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().getList().isEmpty()) {
                    int aqi = response.body().getList().get(0).getMain().getAqi();
                    textViewAQIValue.setText("" + aqi);
                } else {
                    textViewAQIValue.setText("AQI: Data unavailable");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AqiResponse> call, @NonNull Throwable t) {
                textViewAQIValue.setText("AQI: Error fetching data");
            }
        });
    }
    private void updateUI(WeatherResponse weatherResponse) {
        int windSpeed = (int) Math.round(weatherResponse.getWind().getSpeed() * 3.6);
        if (windSpeed == 0) {
            textViewWindSpeed.setText("Calm");
            textViewDirectionSpeed.setText("No wind");
        } else {
            textViewWindSpeed.setText(windSpeed + " km/h");
            textViewDirectionSpeed.setText(weatherResponse.getWind().getDeg() + "° " + weatherResponse.getWind().getDirection());
        }
       // textViewWindSpeed.setText(windSpeed + " km/h");

        textViewTempNow.setText(weatherResponse.getMain().getTemp() + "°C");
        textViewHumidityValue.setText(weatherResponse.getMain().getHumidity() + "%");
       // textViewDirectionSpeed.setText(weatherResponse.getWind().getDeg() + "° " + weatherResponse.getWind().getDirection());

        double temperature = weatherResponse.getMain().getTemp();
        double humidity = weatherResponse.getMain().getHumidity();
        double dewPoint = calculateDewPoint(temperature, humidity);
        textViewHumidityContent.setText("The dew point is " + Math.round(dewPoint) + "° today");

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

    private void updateHourlyForecast(WeatherResponse forecast) {
        // Use the 'list' field which contains the forecast data from the API
        if (forecast == null || forecast.getList() == null || forecast.getList().isEmpty()) {
            Toast.makeText(getContext(), "Hourly forecast data not available", Toast.LENGTH_SHORT).show();
            return;
        }

        java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("ha");

        // Loop through up to 6 forecast data points (3-hour interval each)
        for (int i = 0; i < Math.min(6, forecast.getList().size()); i++) {
            // Assuming FiveDaysWeatherItemModel has methods getDtTxt(), getMain(), and getWeather()
            WeatherResponse.FiveDaysWeatherItemModel forecastItem = forecast.getList().get(i);

            String formattedTime;
            try {
                java.util.Date date = inputFormat.parse(forecastItem.getDt_txt());
                formattedTime = outputFormat.format(date);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
                // Fallback: extract time using split if parsing fails
                formattedTime = forecastItem.getDt_txt().split(" ")[1];
            }

            // Extract the time (assumes dtTxt is in the format "yyyy-MM-dd HH:mm:ss")
            String temp = forecastItem.getMain().getTemp() + "°C";
            String iconUrl = "https://openweathermap.org/img/wn/" + forecastItem.getWeather().get(0).getIcon() + ".png";

            switch (i) {
                case 0:
                    textViewTempNow.setText(temp);
                    textViewHourlyForecast.setText(formattedTime);
                    Glide.with(getContext()).load(iconUrl).into(imageViewHourlyForecastNow);
                    break;
                case 1:
                    textViewTemp2.setText(temp);
                    textViewHourlyForecast2.setText(formattedTime);
                    Glide.with(getContext()).load(iconUrl).into(imageViewHourlyForecast2);
                    break;
                case 2:
                    textViewTemp3.setText(temp);
                    textViewHourlyForecast3.setText(formattedTime);
                    Glide.with(getContext()).load(iconUrl).into(imageViewHourlyForecast3);
                    break;
                case 3:
                    textViewTemp4.setText(temp);
                    textViewHourlyForecast4.setText(formattedTime);
                    Glide.with(getContext()).load(iconUrl).into(imageViewHourlyForecast4);
                    break;
                case 4:
                    textViewTemp5.setText(temp);
                    textViewHourlyForecast5.setText(formattedTime);
                    Glide.with(getContext()).load(iconUrl).into(imageViewHourlyForecast5);
                    break;
                case 5:
                    textViewTemp6.setText(temp);
                    textViewHourlyForecast6.setText(formattedTime);
                    Glide.with(getContext()).load(iconUrl).into(imageViewHourlyForecast6);
                    break;
            }
        }
    }

    private double calculateDewPoint(double temperature, double humidity) {
        return temperature - ((100 - humidity) / 5);
    }
}