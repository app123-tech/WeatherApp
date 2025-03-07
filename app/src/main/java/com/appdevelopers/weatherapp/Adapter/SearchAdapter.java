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
        String formatted = formattedLocations.get(location);
        String shortName;

        if (formatted != null) {
            // Split the full formatted string by commas.
            String[] parts = formatted.split(",");
            // Determine the maximum number of parts to display (max 4)
            int limit = Math.min(parts.length, 4);
            StringBuilder sb = new StringBuilder();
            // Append each part up to the limit, adding a comma between them.
            for (int i = 0; i < limit; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(parts[i].trim());
            }
            shortName = sb.toString();
        } else {
            // Fallback: use available details from GeoLocation.
            shortName = location.getName();
            if (location.getState() != null && !location.getState().isEmpty()) {
                shortName += ", " + location.getState();
            }
        }

        holder.textView.setText(shortName);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(location));
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public void updateList(List<GeoLocation> newLocations) {
        locations.clear();
        locations.addAll(newLocations);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
