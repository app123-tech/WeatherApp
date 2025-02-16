package com.appdevelopers.weatherapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
        private final String[] locations;
        private final OnItemClickListener listener;

        public interface OnItemClickListener {
            void onItemClick(String cityName);
        }

        public SearchAdapter(ArrayList<String> locations, OnItemClickListener listener) {
            this.locations = locations.toArray(new String[0]);
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
            String cityName = locations[position];
            holder.textView.setText(cityName);
            holder.itemView.setOnClickListener(v -> listener.onItemClick(cityName));
        }

        @Override
        public int getItemCount() {
            return locations.length;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
            }
        }
}
