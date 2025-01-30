package com.appdevelopers.weatherapp;

import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class GetStartedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_started);

//        View decoreView = getWindow().getDecorView();
//        decoreView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
//            @NonNull
//            @Override
//            public WindowInsets onApplyWindowInsets(@NonNull View v, @NonNull WindowInsets insets) {
//                int right = insets.getSystemWindowInsetRight();
//                int bottom = insets.getSystemWindowInsetBottom();
//                int left = insets.getSystemWindowInsetLeft();
//                int top = insets.getSystemWindowInsetTop();
//
//                v.setPadding(right, bottom, left, top);
//                return insets.consumeSystemWindowInsets();
//            }
//        });

    }
}