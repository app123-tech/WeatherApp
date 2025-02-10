package com.appdevelopers.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NoInternetActivity extends AppCompatActivity {
    private Button buttonTryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_no_internet);

        buttonTryAgain = findViewById(R.id.buttonTryAgain);

        buttonTryAgain.setOnClickListener(v -> {
            if (NetworkUtils.isConnected(NoInternetActivity.this)){
                Intent intent = new Intent(NoInternetActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}