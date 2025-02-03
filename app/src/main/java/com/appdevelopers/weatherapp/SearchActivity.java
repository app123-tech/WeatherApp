package com.appdevelopers.weatherapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {
    private TextView noLocationFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        noLocationFound = findViewById(R.id.noLocationFound);

        handleSearchResult(false);
    }

    private void handleSearchResult(boolean isLocationFound) {
        if (isLocationFound) {
            noLocationFound.setVisibility(View.GONE);
        } else {
            noLocationFound.setVisibility(View.VISIBLE);
        }
    }
}
