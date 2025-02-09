package com.appdevelopers.weatherapp;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TermsAndConditions extends AppCompatActivity {
    private WebView termsAndConditionsWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_terms_and_conditions);

        termsAndConditionsWebView = findViewById(R.id.termsAndConditionsWebView);
        termsAndConditionsWebView.setWebViewClient(new WebViewClient());
        termsAndConditionsWebView.loadUrl("file:///android_assets/terms.html");
    }
}