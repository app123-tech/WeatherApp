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
    private TextView textViewWind, textViewWindSecond, textViewWindSpeed, textViewAQI, textViewAQISpeed, textViewDirection, textViewDirectionSpeed, textViewHumidity, textViewHumidityValue, textViewHumidityContent;
    private TextView textViewHourlyForecast, textViewHourlyForecastNow, textViewHourlyForecast10AM, textViewHourlyForecast11AM, textViewHourlyForecast12PM, textViewHourlyForecast1PM, textViewHourlyForecast2PM, textViewTempNow, textViewTemp10AM, textViewTemp11AM, textViewTemp12PM, textViewTemp1PM, textViewTemp2PM;
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
}