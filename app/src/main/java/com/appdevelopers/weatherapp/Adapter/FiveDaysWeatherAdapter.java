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

public class FiveDaysWeatherAdapter extends RecyclerView.Adapter<FiveDaysWeatherAdapter.ViewHolder> {
    private List<FiveDaysWeatherItemModel> itemModels;

    public FiveDaysWeatherAdapter(List<FiveDaysWeatherItemModel> itemModels) {
        this.itemModels = itemModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.five_days_weather_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FiveDaysWeatherAdapter.ViewHolder holder, int position) {
        FiveDaysWeatherItemModel item = itemModels.get(position);
        holder.textViewWeatherDays.setText(item.getWeatherDay());
        holder.textViewWeatherDate.setText(item.getWeatherDate());
        holder.textViewWeatherTitle.setText(item.getWeatherTitle());
        holder.textViewMaxTemperature.setText(item.getMaxTemperature());
        holder.textViewMinTemperature.setText(item.getMinTemperature());
        Glide.with(holder.imageViewWeather.getContext())
                .load(item.getWeatherImage())
                .into(holder.imageViewWeather);
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewWeatherDays, textViewWeatherDate, textViewWeatherTitle, textViewMaxTemperature,textViewMinTemperature;
        ImageView imageViewWeather;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWeatherDays = itemView.findViewById(R.id.textViewWeatherDays);
            textViewWeatherDate = itemView.findViewById(R.id.textViewWeatherDate);
            textViewWeatherTitle = itemView.findViewById(R.id.textViewWeatherTitle);
            textViewMaxTemperature = itemView.findViewById(R.id.textViewMaxTemperature);
            textViewMinTemperature = itemView.findViewById(R.id.textViewMinTemperature);
            imageViewWeather = itemView.findViewById(R.id.imageViewWeather);
        }
    }
}
