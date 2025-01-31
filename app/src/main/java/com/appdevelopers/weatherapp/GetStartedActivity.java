package com.appdevelopers.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class GetStartedActivity extends AppCompatActivity {
    private Button buttonGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_started);

        buttonGetStarted = findViewById(R.id.buttonGetStarted);

        buttonGetStarted.setOnClickListener(v -> {
            Intent intent = new Intent(GetStartedActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

    }
}