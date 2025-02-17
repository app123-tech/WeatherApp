package com.appdevelopers.weatherapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
        private final List<String> locations;
        private final OnItemClickListener listener;

        public interface OnItemClickListener {
            void onItemClick(String cityName);
        }

    public SearchAdapter(List<String> locations, OnItemClickListener listener) {
        this.locations = locations;
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
            String cityName = locations.get(position);
            holder.textView.setText(cityName);
            holder.itemView.setOnClickListener(v -> listener.onItemClick(cityName));
        }

        @Override
        public int getItemCount() {
            return locations.size();
        }

    public void updateList(List<String> newLocations) {
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
