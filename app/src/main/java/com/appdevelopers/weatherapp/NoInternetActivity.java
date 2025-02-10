package com.appdevelopers.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NoInternetActivity extends AppCompatActivity {
    private Button buttonTryAgain;
    private ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_no_internet);

        buttonTryAgain = findViewById(R.id.buttonTryAgain);
        imageView2 = findViewById(R.id.imageView2);

        buttonTryAgain.setOnClickListener(v -> {
            if (NetworkUtils.isConnected(NoInternetActivity.this)){
                Intent intent = new Intent(NoInternetActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imageView2.setOnClickListener(v -> {
            Intent intent = new Intent(NoInternetActivity.this, Setting.class);
            startActivity(intent);
        });

    }
}