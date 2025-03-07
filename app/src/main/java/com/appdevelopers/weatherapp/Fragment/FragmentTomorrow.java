package com.appdevelopers.weatherapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class FragmentTomorrow extends Fragment {
    private static final String API_KEY = "151be366b244fa29f3132ffb1b84453d";
    private static final String PREFS_NAME = "WeatherAppPrefs";
    private static final String DEFAULT_CITY = "Kathmandu";
    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceListener;
    private TextView textViewWind, textViewWindSecond, textViewWindSpeed, textViewAQI, textViewAQIValue, textViewDirection, textViewDirectionSpeed, textViewHumidity, textViewHumidityValue, textViewHumidityContent;
    private TextView textView24HourlyForecast, textViewHourlyForecast, textViewHourlyForecast2, textViewHourlyForecast3, textViewHourlyForecast4, textViewHourlyForecast5, textViewHourlyForecast6, textViewTempNow, textViewTemp2, textViewTemp3, textViewTemp4, textViewTemp5, textViewTemp6;
    private TextView textViewSunriseTime, textViewSunsetTime;
    private ImageView imageViewHourlyForecastNow, imageViewHourlyForecast2, imageViewHourlyForecast3, imageViewHourlyForecast4, imageViewHourlyForecast5, imageViewHourlyForecast6;
    private SharedPreferences sharedPreferences;

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

        fetchWeatherForecastTomorrow(DEFAULT_CITY);
    }

    private void fetchWeatherForecastTomorrow(String city) {
        Retrofit retrofit = ApiClient.getClient();
        OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);

        Call<WeatherResponse> call = service.getFiveDayForecastByCity(city, API_KEY, "metric");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse forecastResponse = response.body();
                    updateTomorrowWeatherUI(forecastResponse);
                    if (forecastResponse.getCity() != null && forecastResponse.getCity().getCoord() != null) {
                        fetchTomorrowAqiData(forecastResponse.getCity().getCoord().getLat(), forecastResponse.getCity().getCoord().getLon());
                    } else {
                        Toast.makeText(getContext(), "City location data not available", Toast.LENGTH_SHORT).show();
                    }
                    updateHourlyForecastWeatherTomorrow(forecastResponse);
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

    private String getTemperatureUnit() {
        SharedPreferences appSettings = getActivity().getSharedPreferences("AppSetting", Context.MODE_PRIVATE);
        return appSettings.getString("TemperatureUnit", "C"); // Default to Celsius if not set
    }

    private void fetchTomorrowAqiData(double lat, double lon) {
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
                    textViewAQIValue.setText("AQI: Data not available");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AqiResponse> call, @NonNull Throwable t) {
                textViewAQIValue.setText("AQI: Error fetching data");
            }
        });
    }

    private void updateTomorrowWeatherUI(WeatherResponse forecastResponse) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        String tomorrowDate = dateFormat.format(calendar.getTime());

        WeatherResponse.ForecastItem tomorrowForecast = null;
        for (WeatherResponse.ForecastItem item : forecastResponse.getList()) {
            if (item.getDt_txt().startsWith(tomorrowDate)) {
                tomorrowForecast = item;
                break;
            }
        }

        if (tomorrowForecast == null) {
            Toast.makeText(getContext(), "Tomorrow's forecast data not available", Toast.LENGTH_SHORT).show();
            return;
        }

        String tempUnit = getTemperatureUnit();
        double temp = tomorrowForecast.getMain().getTemp();
        if (tempUnit.equals("F")) {
            temp = (temp * 9 / 5) + 32; // Convert Celsius to Fahrenheit
        }

        if (forecastResponse.getWind() != null) {
            int windSpeed = (int) Math.round(forecastResponse.getWind().getSpeed() * 3.6);
            if (windSpeed == 0) {
                textViewWindSpeed.setText("Calm");
                textViewDirectionSpeed.setText("No wind");
            } else {
                textViewWindSpeed.setText(windSpeed + " km/h");
                textViewDirectionSpeed.setText(forecastResponse.getWind().getDeg() + "° "
                        + forecastResponse.getWind().getDirection());
            }
        } else {
            textViewWindSpeed.setText("N/A");
            textViewDirectionSpeed.setText("N/A");
        }
        // textViewWindSpeed.setText(windSpeed + " km/h");

        textViewTempNow.setText(String.format("%.1f°%s", temp, tempUnit));
        textViewHumidityValue.setText(tomorrowForecast.getMain().getHumidity() + "%");
        // textViewDirectionSpeed.setText(weatherResponse.getWind().getDeg() + "° " + weatherResponse.getWind().getDirection());

        //double temperature = tomorrowForecast.getMain().getTemp();
        double humidity = tomorrowForecast.getMain().getHumidity();
        double dewPoint = calculateDewPoint(tomorrowForecast.getMain().getTemp(), humidity);
        if (tempUnit.equals("F")) {
            dewPoint = (dewPoint * 9 / 5) + 32;
        }
        textViewHumidityContent.setText(String.format("The dew point is %.1f°%s tomorrow", dewPoint, tempUnit));


        // Handle sunrise and sunset times
        if (forecastResponse.getSys() != null) {
            long sunriseTime = forecastResponse.getSys().getSunrise();
            long sunsetTime = forecastResponse.getSys().getSunset();
            Log.d("Sunrise", "Sunrise timestamp: " + sunriseTime);
            Log.d("Sunset", "Sunset timestamp: " + sunsetTime);
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
            String sunrise = new java.text.SimpleDateFormat("hh:mm a").format(new java.util.Date(sunriseTime * 1000));
            String sunset = new java.text.SimpleDateFormat("hh:mm a").format(new java.util.Date(sunsetTime * 1000));
            textViewSunriseTime.setText(sunrise);
            textViewSunsetTime.setText(sunset);
        } else {
            textViewSunriseTime.setText("N/A");
            textViewSunsetTime.setText("N/A");
        }
    }

    private void updateHourlyForecastWeatherTomorrow(WeatherResponse forecastResponse) {
        String tempUnit = getTemperatureUnit();
        // Use the 'list' field which contains the forecast data from the API
        if (forecastResponse == null || forecastResponse.getList() == null || forecastResponse.getList().isEmpty()) {
            Toast.makeText(getContext(), "Hourly forecast data not available", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        String tomorrowDate = dateFormat.format(calendar.getTime());

        List<WeatherResponse.ForecastItem> tomorrowItems = new ArrayList<>();
        for (WeatherResponse.ForecastItem item : forecastResponse.getList()) {
            if (item.getDt_txt().startsWith(tomorrowDate)) {
                tomorrowItems.add(item);
            }
        }
        if (tomorrowItems.isEmpty()) {
            Toast.makeText(getContext(), "No hourly forecast data available for tomorrow", Toast.LENGTH_SHORT).show();
            return;
        }

        java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("ha");

        // Loop through up to 6 forecast data points (3-hour interval each)
        for (int i = 0; i < Math.min(6, tomorrowItems.size()); i++) {
            // Assuming ForecastItem has methods getDtTxt(), getMain(), and getWeather()
            WeatherResponse.ForecastItem forecastItem = tomorrowItems.get(i);
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
            double temp = forecastItem.getMain().getTemp();
            if (tempUnit.equals("F")) {
                temp = (temp * 9 / 5) + 32;
            }
            String tempDisplay = String.format("%.1f°%s", temp, tempUnit);

            String iconUrl = "https://openweathermap.org/img/wn/" + forecastItem.getWeather().get(0).getIcon() + ".png";

            switch (i) {
                case 0:
                    textViewTempNow.setText(tempDisplay);
                    textViewHourlyForecast.setText(formattedTime);
                    Glide.with(getContext()).load(iconUrl).into(imageViewHourlyForecastNow);
                    break;
                case 1:
                    textViewTemp2.setText(tempDisplay);
                    textViewHourlyForecast2.setText(formattedTime);
                    Glide.with(getContext()).load(iconUrl).into(imageViewHourlyForecast2);
                    break;
                case 2:
                    textViewTemp3.setText(tempDisplay);
                    textViewHourlyForecast3.setText(formattedTime);
                    Glide.with(getContext()).load(iconUrl).into(imageViewHourlyForecast3);
                    break;
                case 3:
                    textViewTemp4.setText(tempDisplay);
                    textViewHourlyForecast4.setText(formattedTime);
                    Glide.with(getContext()).load(iconUrl).into(imageViewHourlyForecast4);
                    break;
                case 4:
                    textViewTemp5.setText(tempDisplay);
                    textViewHourlyForecast5.setText(formattedTime);
                    Glide.with(getContext()).load(iconUrl).into(imageViewHourlyForecast5);
                    break;
                case 5:
                    textViewTemp6.setText(tempDisplay);
                    textViewHourlyForecast6.setText(formattedTime);
                    Glide.with(getContext()).load(iconUrl).into(imageViewHourlyForecast6);
                    break;
            }
        }
    }

    private double calculateDewPoint(double temperature, double humidity) {
        return temperature - ((100 - humidity) / 5);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register listener
        SharedPreferences appSettings = getActivity().getSharedPreferences("AppSetting", Context.MODE_PRIVATE);
        appSettings.registerOnSharedPreferenceChangeListener(sharedPreferenceListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister listener
        SharedPreferences appSettings = getActivity().getSharedPreferences("AppSetting", Context.MODE_PRIVATE);
        appSettings.unregisterOnSharedPreferenceChangeListener(sharedPreferenceListener);
    }
}