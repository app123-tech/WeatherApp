package com.appdevelopers.weatherapp;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Setting extends AppCompatActivity {
    private ImageView imageViewBack, imageViewGreaterThanCircle5, imageViewGreaterThanCircle, imageViewGreaterThanCircle2, imageViewGreaterThanCircle3, imageViewGreaterThanCircle4;
    private CardView cardView2, cardView3, cardView4, cardView5, cardView6, cardView7, cardView8, cardView9, cardView10;
    private TextView textViewTemperature, textViewWindSpeedInKilometerPerHour, textViewLanguage;
    private Switch toggleSwitch;
    private int thumbOnColor;
    private int thumbOffColor;
    private int trackOnColor;
    private int trackOffColor;


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

        toggleSwitch = findViewById(R.id.toggleSwitch);

        thumbOnColor = ContextCompat.getColor(this, R.color.switch_thumb_on);
        thumbOffColor = ContextCompat.getColor(this, R.color.switch_thumb_off);
        trackOnColor = ContextCompat.getColor(this, R.color.switch_track_on);
        trackOffColor = ContextCompat.getColor(this, R.color.switch_track_off);

        cardView10.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, AboutOurApp.class);
            startActivity(intent);
        });

        imageViewGreaterThanCircle.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, PrivacyPolicy.class);
            startActivity(intent);
        });

        cardView7.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, PrivacyPolicy.class);
            startActivity(intent);
        });

        imageViewGreaterThanCircle5.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, PrivacyPolicy.class);
            startActivity(intent);
        });

        cardView6.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, PrivacyPolicy.class);
            startActivity(intent);
        });

        cardView9.setOnClickListener(v -> {
            try {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(Setting.this, "App not found on Play Store.", Toast.LENGTH_SHORT).show();
            }
        });

        imageViewGreaterThanCircle3.setOnClickListener(v -> {
            try {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(Setting.this, "App not found on Play Store.", Toast.LENGTH_SHORT).show();
            }
        });

        cardView8.setOnClickListener(v -> {
            String appPackageName = getPackageName();
            String playStoreLink = "https://play.google.com/store/apps/details?id=" + appPackageName;
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareMessage = "Check out this amazing Weather App: " + playStoreLink;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            try {
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(Setting.this, "No application found to share the app.", Toast.LENGTH_SHORT).show();
            }
        });

        cardView5.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
            android.view.LayoutInflater inflater = getLayoutInflater();
            android.view.View dialogView = inflater.inflate(R.layout.custom_multi_language_selection, null);
            builder.setView(dialogView);

            android.widget.RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);
            android.widget.TextView textViewAccept = dialogView.findViewById(R.id.textViewAccept);
            android.widget.TextView textViewCancel = dialogView.findViewById(R.id.textViewCancel);

            android.app.AlertDialog dialog = builder.create();

            SharedPreferences sharedPreferences = getSharedPreferences("AppSetting", MODE_PRIVATE);
            String selectedLanguage = sharedPreferences.getString("Language", "English");

            if (selectedLanguage.equals("English")) {
                radioGroup.check(R.id.radioButtonEnglish);
            } else if (selectedLanguage.equals("Nepali")) {
                radioGroup.check(R.id.radioButtonNepali);
            } else if (selectedLanguage.equals("Italian")) {
                radioGroup.check(R.id.radioButtonItalian);
            } else if (selectedLanguage.equals("Korean")) {
                radioGroup.check(R.id.radioButtonKorean);
            }

            textViewAccept.setOnClickListener(v1 -> {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                String newLanguage = "English";

                if (selectedId == R.id.radioButtonNepali) {
                    newLanguage = "Nepali";
                } else if (selectedId == R.id.radioButtonItalian) {
                    newLanguage = "Italian";
                } else if (selectedId == R.id.radioButtonKorean) {
                    newLanguage = "Korean";
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Language", newLanguage);
                editor.apply();
            });
            textViewCancel.setOnClickListener(v1 -> {
                dialog.dismiss();
            });
            dialog.show();
        });
    }

    private void setLocale(String languageCode){
        android.content.res.Configuration config = new android.content.res.Configuration();
        java.util.Locale locale = new java.util.Locale(languageCode);
        java.util.Locale.setDefault(locale);
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}