package com.appdevelopers.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Setting extends AppCompatActivity {
    private ImageView imageViewBack, imageViewGreaterThanCircle5, imageViewGreaterThanCircle, imageViewGreaterThanCircle2, imageViewGreaterThanCircle3, imageViewGreaterThanCircle4;
    private CardView cardView2, cardView3, cardView4, cardView5, cardView6, cardView7, cardView8, cardView9, cardView10;
    private TextView textViewTemperature, textViewWindSpeedInKilometerPerHour, textViewLanguage;
    private Switch switch1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);

        imageViewBack = findViewById(R.id.imageViewBack);
        imageViewGreaterThanCircle = findViewById(R.id.imageViewGreaterThanCircle);     //Privacy Policy
        imageViewGreaterThanCircle2 = findViewById(R.id.imageViewGreaterThanCircle2);   //Share Our App
        imageViewGreaterThanCircle3 = findViewById(R.id.imageViewGreaterThanCircle3);   //Rate Our App
        imageViewGreaterThanCircle4 = findViewById(R.id.imageViewGreaterThanCircle4);   //About Our App
        imageViewGreaterThanCircle5 = findViewById(R.id.imageViewGreaterThanCircle5);   //Terms & Conditions

        cardView2 = findViewById(R.id.cardView2);       //Temperature Unit
        cardView3 = findViewById(R.id.cardView3);       //Wind Speed Unit
        cardView4 = findViewById(R.id.cardView4);       //Daily Forecast Notifications
        cardView5 = findViewById(R.id.cardView5);       //Change App Language
        cardView6 = findViewById(R.id.cardView6);       //Terms & Conditions
        cardView7 = findViewById(R.id.cardView7);       //Privacy Policy
        cardView8 = findViewById(R.id.cardView8);       //Share Our App
        cardView9 = findViewById(R.id.cardView9);       //Rate Our App
        cardView10 = findViewById(R.id.cardView10);       //About Our App

        textViewTemperature = findViewById(R.id.textViewTemperature);
        textViewWindSpeedInKilometerPerHour = findViewById(R.id.textViewWindSpeedInKilometerPerHour);
        textViewLanguage = findViewById(R.id.textViewLanguage);

        switch1 = findViewById(R.id.switch1);

        cardView10.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, AboutOurApp.class);
            startActivity(intent);
        });

    }
}