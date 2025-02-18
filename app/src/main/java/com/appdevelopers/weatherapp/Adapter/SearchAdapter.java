package com.appdevelopers.weatherapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdevelopers.weatherapp.Model.GeoLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private final List<GeoLocation> locations;
    private final Map<GeoLocation, String> formattedLocations;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(GeoLocation location);
    }

    public SearchAdapter(List<GeoLocation> locations, Map<GeoLocation, String> formattedLocations, OnItemClickListener listener) {
        this.locations = locations;
        this.formattedLocations = formattedLocations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GeoLocation location = locations.get(position);
        // Get the formatted string; if missing, fallback to a basic format.
        String formatted = formattedLocations.get(location);
        if (formatted == null) {
            formatted = location.getName() +
                    (location.getState() != null && !location.getState().isEmpty() ? ", " + location.getState() : "") +
                    ", " + location.getCountry();
        }
        holder.textView.setText(formatted);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(location));
    }


    @Override
    public int getItemCount() {
        return locations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
