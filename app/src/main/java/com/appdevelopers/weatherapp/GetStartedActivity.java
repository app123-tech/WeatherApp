package com.appdevelopers.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
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
        SharedPreferences sharedPreferences =   getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);

        if (!isFirstTime) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_started);
        buttonGetStarted = findViewById(R.id.buttonGetStarted);

        buttonGetStarted.setOnClickListener(v -> {
          SharedPreferences.Editor editor = sharedPreferences.edit();
          editor.putBoolean("isFirstTime", false);
          editor.apply();

          Intent intent = new Intent(GetStartedActivity.this, MainActivity.class);
          startActivity(intent);
          finish();
        });
    }
}