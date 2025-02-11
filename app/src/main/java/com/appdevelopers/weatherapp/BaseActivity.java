package com.appdevelopers.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences sharedPreferences = newBase.getSharedPreferences("AppSetting", MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("Language", "en"); // Use "en" as default
        super.attachBaseContext(LocaleHelper.setLocale(newBase, languageCode));
    }
}
