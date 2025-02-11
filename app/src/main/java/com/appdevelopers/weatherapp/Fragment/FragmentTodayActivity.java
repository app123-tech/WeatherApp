package com.appdevelopers.weatherapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdevelopers.weatherapp.R;

public class FragmentTodayActivity extends Fragment {
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "2e20e6e6c10856f71e0312f1ca503ac9";
    private TextView textViewWind, textViewWindSecond, textViewWindSpeed, textViewAQI, textViewAQISpeed, textViewDirection, textViewDirectionSpeed, textViewHumidity, textViewHumidityValue, textViewHumidityContent;
    private TextView textViewHourlyForecast, textViewHourlyForecastNow, textViewHourlyForecast10AM, textViewHourlyForecast11AM, textViewHourlyForecast12PM, textViewHourlyForecast1PM, textViewHourlyForecast2PM, textViewTempNow, textViewTemp10AM, textViewTemp11AM, textViewTemp12PM, textViewTemp1PM, textViewTemp2PM;
    private TextView textViewSunriseTime, textViewSunsetTime;
    private ImageView imageViewHourlyForecastNow, imageViewHourlyForecast10AM, imageViewHourlyForecast11AM, imageViewHourlyForecast12PM, imageViewHourlyForecast1PM, imageViewHourlyForecast2PM;

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
        textViewWind = view.findViewById(R.id.textViewWind);
        textViewWindSecond = view.findViewById(R.id.textViewWindSecond);
        textViewWindSpeed = view.findViewById(R.id.textViewWindSpeed);
        textViewAQI = view.findViewById(R.id.textViewAQI);
        textViewAQISpeed = view.findViewById(R.id.textViewAQISpeed);
        textViewDirection = view.findViewById(R.id.textViewDirection);
        textViewDirectionSpeed = view.findViewById(R.id.textViewDirectionSpeed);
        textViewHumidity = view.findViewById(R.id.textViewHumidity);
        textViewHumidityValue = view.findViewById(R.id.textViewHumidityValue);
        textViewHumidityContent = view.findViewById(R.id.textViewHumidityContent);

        textViewHourlyForecast = view.findViewById(R.id.textViewHourlyForecast);            // not necessary
        textViewHourlyForecastNow = view.findViewById(R.id.textViewHourlyForecastNow);      // not necessary
        textViewHourlyForecast10AM = view.findViewById(R.id.textViewHourlyForecast10AM);    // not necessary
        textViewHourlyForecast11AM = view.findViewById(R.id.textViewHourlyForecast11AM);    // not necessary
        textViewHourlyForecast12PM = view.findViewById(R.id.textViewHourlyForecast12PM);    // not necessary
        textViewHourlyForecast1PM = view.findViewById(R.id.textViewHourlyForecast1PM);      // not necessary
        textViewHourlyForecast2PM = view.findViewById(R.id.textViewHourlyForecast2PM);      // not necessary

        textViewTempNow = view.findViewById(R.id.textViewTempNow);
        textViewTemp10AM = view.findViewById(R.id.textViewTemp10AM);
        textViewTemp11AM = view.findViewById(R.id.textViewTemp11AM);
        textViewTemp12PM = view.findViewById(R.id.textViewTemp12PM);
        textViewTemp1PM = view.findViewById(R.id.textViewTemp1PM);
        textViewTemp2PM = view.findViewById(R.id.textViewTemp2PM);

        textViewSunriseTime = view.findViewById(R.id.textViewSunriseTime);
        textViewSunsetTime = view.findViewById(R.id.textViewSunsetTime);

        // Initializing ImageViews
        imageViewHourlyForecastNow = view.findViewById(R.id.imageViewHourlyForecastNow);
        imageViewHourlyForecast10AM = view.findViewById(R.id.imageViewHourlyForecast10AM);
        imageViewHourlyForecast11AM = view.findViewById(R.id.imageViewHourlyForecast11AM);
        imageViewHourlyForecast12PM = view.findViewById(R.id.imageViewHourlyForecast12PM);
        imageViewHourlyForecast1PM = view.findViewById(R.id.imageViewHourlyForecast1PM);
        imageViewHourlyForecast2PM = view.findViewById(R.id.imageViewHourlyForecast2PM);
    }
}