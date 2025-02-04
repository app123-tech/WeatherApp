package com.appdevelopers.weatherapp.Adapter;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdevelopers.weatherapp.Model.tenDaysWeatherItemModel;
import com.appdevelopers.weatherapp.R;

import java.util.List;

public class tenDaysWeatherAdapter extends RecyclerView.Adapter<tenDaysWeatherAdapter.tenDaysWeatherViewHolder> {
    private List<tenDaysWeatherItemModel> itemModels;

    public tenDaysWeatherAdapter(List<tenDaysWeatherItemModel> itemModels) {
        this.itemModels = itemModels;
    }

    @NonNull
    @Override
    public tenDaysWeatherAdapter.tenDaysWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ten_days_weather_item, parent, false)
        return new tenDaysWeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tenDaysWeatherAdapter.tenDaysWeatherViewHolder holder, int position) {
        tenDaysWeatherItemModel item = itemModels.get(position);
        tenDaysWeatherViewHolder.textViewWeatherDays.setText(item.getWeatherDay());
        tenDaysWeatherViewHolder.textViewWeatherTitle.setText(item.getWeatherTitle());
        tenDaysWeatherViewHolder.textViewMaxTemperature.setText(item.getMaxTemperature());
        tenDaysWeatherViewHolder.textViewMinTemperature.setText(item.getMinTemperature());
        tenDaysWeatherViewHolder.imageViewWeather.setText(item.getWeatherImage());

    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    public class tenDaysWeatherViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewWeatherDays, textViewWeatherTitle, textViewMaxTemperature,textViewMinTemperature;
        private ImageView imageViewWeather;
        public tenDaysWeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWeatherDays = itemView.findViewById(R.id.textViewWeatherDays);
            textViewWeatherTitle = itemView.findViewById(R.id.textViewWeatherTitle);
            textViewMaxTemperature = itemView.findViewById(R.id.textViewMaxTemperature);
            textViewMinTemperature = itemView.findViewById(R.id.textViewMinTemperature);
            imageViewWeather = itemView.findViewById(R.id.imageViewWeather);
        }
    }
}
