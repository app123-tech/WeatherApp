package com.appdevelopers.weatherapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdevelopers.weatherapp.R;


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
        textViewHourlyForecastNowTemp = view.findViewById(R.id.textViewHourlyForecastNowTemp);
        textViewHourlyForecast10AMTemp = view.findViewById(R.id.textViewHourlyForecast10AMTemp);
        textViewHourlyForecast11AMTemp = view.findViewById(R.id.textViewHourlyForecast11AMTemp);
        textViewHourlyForecast12PMTemp = view.findViewById(R.id.textViewHourlyForecast12PMTemp);
        textViewHourlyForecast1PMTemp = view.findViewById(R.id.textViewHourlyForecast1PMTemp);
        textViewHourlyForecast2PMTemp = view.findViewById(R.id.textViewHourlyForecast2PMTemp);
        textViewSunriseTime = view.findViewById(R.id.textViewSunriseTime);
        textViewSunsetTime = view.findViewById(R.id.textViewSunsetTime);
        imageViewHourlyForecastNow = view.findViewById(R.id.imageViewHourlyForecastNow);
        imageViewHourlyForecast10AM = view.findViewById(R.id.imageViewHourlyForecast10AM);
        imageViewHourlyForecast11AM = view.findViewById(R.id.imageViewHourlyForecast11AM);
        imageViewHourlyForecast12PM = view.findViewById(R.id.imageViewHourlyForecast12PM);
        imageViewHourlyForecast1PM = view.findViewById(R.id.imageViewHourlyForecast1PM);
        imageViewHourlyForecast2PM = view.findViewById(R.id.imageViewHourlyForecast2PM);

    }
}