package com.appdevelopers.weatherapp;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PrivacyPolicy extends AppCompatActivity {
    private WebView privacyPolicyWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_privacy_policy);
       privacyPolicyWebView = findViewById(R.id.privacyPolicyWebView);
       privacyPolicyWebView.setWebViewClient(new WebViewClient());
       privacyPolicyWebView.loadUrl("D:\\Android\\WeatherApp\\app\\src\\main\\assets\\terms.html");
    }
}