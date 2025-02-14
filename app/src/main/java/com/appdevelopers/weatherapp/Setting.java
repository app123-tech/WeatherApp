package com.appdevelopers.weatherapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
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

import java.util.Calendar;

public class Setting extends AppCompatActivity {
    private ImageView imageViewBack, imageViewGreaterThanCircle5, imageViewGreaterThanCircle, imageViewGreaterThanCircle2, imageViewGreaterThanCircle3, imageViewGreaterThanCircle4;
    private CardView cardView2, cardView3, cardView4, cardView5, cardView6, cardView7, cardView8, cardView9, cardView10;
    private TextView textViewTemperature, textViewWindSpeedInKilometerPerHour, textViewLanguage, textViewVersion;
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

        cardView2 = findViewById(R.id.cardView2);       //Temperature Unit C (Celsius) F (Fahrenheit)
        cardView3 = findViewById(R.id.cardView3);       //Wind Speed Unit km/h (kilometer per hour) m/h (meter per hour)
        cardView4 = findViewById(R.id.cardView4);       //Daily Forecast Notifications
        cardView5 = findViewById(R.id.cardView5);       //Change App Language
        cardView6 = findViewById(R.id.cardView6);       //Terms & Conditions
        cardView7 = findViewById(R.id.cardView7);       //Privacy Policy
        cardView8 = findViewById(R.id.cardView8);       //Share Our App
        cardView9 = findViewById(R.id.cardView9);       //Rate Our App
        cardView10 = findViewById(R.id.cardView10);     //About Our App

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
            AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
            dialog.setOnShowListener(dialogInterface -> {
                Window window = dialog.getWindow();
                if (window != null) {
                    window.setLayout(int) (getResources().getDisplayMetrics().widthPixels * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);
                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setCornerRadius(60);
                    drawable.setColor(Color.WHITE);

                    window.setBackgroundDrawable(drawable);
                }
            });
            //AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
            android.view.LayoutInflater inflater = getLayoutInflater();
            android.view.View dialogView = inflater.inflate(R.layout.custom_multi_language_selection, null);
            builder.setView(dialogView);

            android.widget.RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);
            android.widget.TextView textViewAccept = dialogView.findViewById(R.id.textViewAccept);
            android.widget.TextView textViewCancel = dialogView.findViewById(R.id.textViewCancel);

            android.app.AlertDialog dialog = builder.create();

            SharedPreferences sharedPreferences = getSharedPreferences("AppSetting", MODE_PRIVATE);
            String selectedLanguageCode = sharedPreferences.getString("Language", "en"); // Default to "en"

            // Check the corresponding radio button based on the stored language code
            if (selectedLanguageCode.equals("en")) {
                radioGroup.check(R.id.radioButtonEnglish);
            } else if (selectedLanguageCode.equals("ne")) {
                radioGroup.check(R.id.radioButtonNepali);
            } else if (selectedLanguageCode.equals("it")) {
                radioGroup.check(R.id.radioButtonItalian);
            } else if (selectedLanguageCode.equals("ko")) {
                radioGroup.check(R.id.radioButtonKorean);
            }

            textViewAccept.setOnClickListener(v1 -> {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                String newLanguageCode = "en"; // Default to English

                if (selectedId == R.id.radioButtonNepali) {
                    newLanguageCode = "ne";
                } else if (selectedId == R.id.radioButtonItalian) {
                    newLanguageCode = "it";
                } else if (selectedId == R.id.radioButtonKorean) {
                    newLanguageCode = "ko";
                }
                setLocale(newLanguageCode);
                dialog.dismiss();
            });
            textViewCancel.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });

        cardView4.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("AppSetting", MODE_PRIVATE);
            boolean isEnabled = sharedPreferences.getBoolean("DailyForecastNotification", false);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (!isEnabled) {
                scheduleDailyNotification();
                Toast.makeText(Setting.this, "Daily forecast notifications enabled.", Toast.LENGTH_SHORT).show();
            } else {
                cancelDailyNotification();
                Toast.makeText(Setting.this, "Daily forecast notifications disabled.", Toast.LENGTH_SHORT).show();
            }
            editor.putBoolean("DailyForecastNotification", !isEnabled);
            editor.apply();
        });

        cardView2.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
            dialog.setOnShowListener(dialogInterface ->{
                Window window = dialog.getWindow();
                if (window != null) {
                    window.setLayout(int) (getResources().getDisplayMetrics().widthPixels *0.90), WindowManager.LayoutParams.WRAP_CONTENT);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(60);
            drawable.setColor(Color.WHITE);

            window.setBackgroundDrawable(drawable);
                }
            });
        });

        cardView3.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
            dialog.setOnShowListener(dialogInterface ->{
                Window window = dialog.getWindow();
                if (window.setLayout(int) (getResources().getDisplayMetrics().widthPixels * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);
                GradientDrawable drawable = new GradientDrawable();
                drawable.setCornerRadius(60);
                drawable.setColor(Color.WHITE);
            });
        });

        imageViewBack.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }

    void scheduleDailyNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1); // If it's already past 7 AM today, set for tomorrow
        }

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void cancelDailyNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
    }

    public class BaseActivity extends AppCompatActivity {
        @Override
        protected void attachBaseContext(Context newBase) {
            SharedPreferences sharedPreferences = newBase.getSharedPreferences("AppSetting", MODE_PRIVATE);
            String languageCode = sharedPreferences.getString("Language", "en"); // Use "en" as default
            super.attachBaseContext(LocaleHelper.setLocale(newBase, languageCode));
        }
    }

    private void setLocale(String languageCode) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppSetting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Language", languageCode);
        editor.apply();

        LocaleHelper.setLocale(this, languageCode);  // Apply language using helper
        //recreate();  // Restart activity to apply language change
    }
}