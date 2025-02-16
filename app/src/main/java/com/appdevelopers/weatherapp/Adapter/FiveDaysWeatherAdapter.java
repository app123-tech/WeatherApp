package com.appdevelopers.weatherapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdevelopers.weatherapp.Model.FiveDaysWeatherItemModel;
import com.appdevelopers.weatherapp.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class FiveDaysWeatherAdapter extends RecyclerView.Adapter<FiveDaysWeatherAdapter.tenDaysWeatherViewHolder> {
    private List<FiveDaysWeatherItemModel> itemModels;

    public FiveDaysWeatherAdapter(List<FiveDaysWeatherItemModel> itemModels) {
        this.itemModels = itemModels;
    }

    @NonNull
    @Override
    public tenDaysWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ten_days_weather_item, parent, false);
        return new tenDaysWeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tenDaysWeatherViewHolder holder, int position) {
        FiveDaysWeatherItemModel item = itemModels.get(position);
        holder.textViewWeatherDays.setText(item.getWeatherDay());
        holder.textViewWeatherTitle.setText(item.getWeatherTitle());
        holder.textViewMaxTemperature.setText(item.getMaxTemperature());
        holder.textViewMinTemperature.setText(item.getMinTemperature());
        Glide.with(holder.imageViewWeather.getContext())
                .load(item.getWeatherImage()) // The URL for the weather icon
                .into(holder.imageViewWeather);
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    public class tenDaysWeatherViewHolder extends RecyclerView.ViewHolder {
        TextView textViewWeatherDays, textViewWeatherTitle, textViewMaxTemperature,textViewMinTemperature;
        ImageView imageViewWeather;
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
