package com.appdevelopers.weatherapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Calendar;
import java.util.Locale;

public class Setting extends BaseActivity {
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
        textViewVersion = findViewById(R.id.textViewVersion);

        updateVersionNumber();
        updateLanguageTextView();

        thumbOnColor = ContextCompat.getColor(this, R.color.switch_thumb_on);
        thumbOffColor = ContextCompat.getColor(this, R.color.switch_thumb_off);
        trackOnColor = ContextCompat.getColor(this, R.color.switch_track_on);
        trackOffColor = ContextCompat.getColor(this, R.color.switch_track_off);

        SharedPreferences sharedPreferences = getSharedPreferences("AppSetting", MODE_PRIVATE);
        boolean isEnabled = sharedPreferences.getBoolean("DailyForecastNotification", false);
        toggleSwitch.setChecked(isEnabled);
        toggleSwitch.setThumbTintList(isEnabled ? ColorStateList.valueOf(thumbOnColor) : ColorStateList.valueOf(thumbOffColor));
        toggleSwitch.setTrackTintList(isEnabled ? ColorStateList.valueOf(trackOnColor) : ColorStateList.valueOf(trackOffColor));
        String temperatureUnit = getTemperatureUnit();
        textViewTemperature.setText(temperatureUnit.equals("C") ? "°C" : "°F");

        // Load saved wind speed unit preference
        String windSpeedUnit = getWindSpeedUnit();
        textViewWindSpeedInKilometerPerHour.setText(windSpeedUnit.equals("km/h") ? "km/h" : "m/s");

        toggleSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            Switch mySwitch = (Switch) compoundButton;  // Cast to Switch
            if (isChecked) {
                scheduleDailyNotification();
                mySwitch.setThumbTintList(ColorStateList.valueOf(thumbOnColor));
                mySwitch.setTrackTintList(ColorStateList.valueOf(trackOnColor));
                Toast.makeText(Setting.this, "Daily forecast notifications enabled.", Toast.LENGTH_SHORT).show();
            } else {
                cancelDailyNotification();
                mySwitch.setThumbTintList(ColorStateList.valueOf(thumbOffColor));
                mySwitch.setTrackTintList(ColorStateList.valueOf(trackOffColor));
                Toast.makeText(Setting.this, "Daily forecast notifications disabled.", Toast.LENGTH_SHORT).show();
            }
           // SharedPreferences sharedPreferences = getSharedPreferences("AppSetting", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("DailyForecastNotification", isChecked);
            editor.apply();
        });
        
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
           showLanguageSelectionDialog();
        });

        cardView4.setOnClickListener(v -> toggleSwitch.setChecked(!toggleSwitch.isChecked()));

        cardView2.setOnClickListener(v -> {
           showTempUnitSelectionDialog();
        });

        cardView3.setOnClickListener(v -> {
          showWindUnitSelectionDialog();
        });

        imageViewBack.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }

    private void showTempUnitSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Temperature Unit");
        builder.setSingleChoiceItems(new String[]{"Celsius (°C)", "Fahrenheit (°F)"},
                getTemperatureUnit().equals("C") ? 0 : 1, (dialog, which) -> {
                    String newUnit = (which == 0) ? "C" : "F";
                    saveTemperatureUnit(newUnit);
                    convertTemperatureValues(newUnit);
                    dialog.dismiss();
                    Intent intent = new Intent(Setting.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                });
        builder.show();
    }

    private void showWindUnitSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Wind Speed Unit");
        builder.setSingleChoiceItems(new String[]{"Kilometers per hour (km/h)", "Meters per second (m/s)"},
                getWindSpeedUnit().equals("km/h") ? 0 : 1, (dialog, which) -> {
                    String newUnit = (which == 0) ? "km/h" : "m/s";
                    saveWindSpeedUnit(newUnit);
                    convertWindSpeedValues(newUnit);

                    Intent broadcastIntent = new Intent("WIND_SPEED_UNIT_CHANGED");
                    broadcastIntent.putExtra("WIND_SPEED_UNIT", newUnit);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
                    dialog.dismiss();
                    // Restart MainActivity to apply changes immediately
                    Intent intent = new Intent(Setting.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                });
        builder.show();
    }

    private void showLanguageSelectionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        android.view.LayoutInflater inflater = getLayoutInflater();
        android.view.View dialogView = inflater.inflate(R.layout.custom_multi_language_selection, null);
        builder.setView(dialogView);

        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);
        TextView textViewAccept = dialogView.findViewById(R.id.textViewAccept);
        TextView textViewCancel = dialogView.findViewById(R.id.textViewCancel);

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);
                GradientDrawable drawable = new GradientDrawable();
                drawable.setCornerRadius(60);
                drawable.setColor(Color.WHITE);
                window.setBackgroundDrawable(drawable);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("AppSetting", MODE_PRIVATE);
        String selectedLanguageCode = sharedPreferences.getString("Language", "en");
        if (selectedLanguageCode.equals("en")){
            radioGroup.check(R.id.radioButtonEnglish);
        }else if (selectedLanguageCode.equals("ne")){
            radioGroup.check(R.id.radioButtonNepali);
        } else if (selectedLanguageCode.equals("it")) {
            radioGroup.check(R.id.radioButtonItalian);
        } else if (selectedLanguageCode.equals("ko")) {
            radioGroup.check(R.id.radioButtonKorean);
        }

        textViewAccept.setOnClickListener(view -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            String newLanguageCode = "en";

            if (selectedId == R.id.radioButtonNepali){
                newLanguageCode = "ne";
            } else if (selectedId == R.id.radioButtonItalian){
                newLanguageCode = "it";
            } else if (selectedId == R.id.radioButtonKorean){
                newLanguageCode = "ko";
            }

            setLocale(newLanguageCode);
            updateLanguageTextView();
            dialog.dismiss();
        });

        textViewCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
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

        LocaleHelper.setLocale(this, languageCode);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("isLanguageChange", true); // Pass the flag
        // Clear the entire activity stack and start a new task
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        // Finish all current activities
        finishAffinity();
    }

    private void updateLanguageTextView() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppSetting", MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("Language", "en");
        String languageDisplay;
        switch (languageCode) {
            case "ne":
                languageDisplay = "नेपाली";
                break;
            case "it":
                languageDisplay = "Italian";
                break;
            case "ko":
                languageDisplay = "한국어";
                break;
            default:
                languageDisplay = "English";
        }
        textViewLanguage.setText(languageDisplay);
    }

    public void updateVersionNumber() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String VersionName = packageInfo.versionName;
            textViewVersion.setText("Version " + VersionName);
        } catch (Exception e) {
            textViewVersion.setText("Version is not available");
        }
    }

    private void saveTemperatureUnit(String unit) {
        SharedPreferences.Editor editor = getSharedPreferences("AppSetting", MODE_PRIVATE).edit();
        editor.putString("TemperatureUnit", unit);
        editor.apply();
        textViewTemperature.setText(unit.equals("C") ? "°C" : "°F");
    }

    private void saveWindSpeedUnit(String unit) {
        SharedPreferences.Editor editor = getSharedPreferences("AppSetting", MODE_PRIVATE).edit();
        editor.putString("WindSpeedUnit", unit);
        editor.apply();
        textViewWindSpeedInKilometerPerHour.setText(unit.equals("km/h") ? "km/h" : "m/s");
    }

    private String getTemperatureUnit() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppSetting", MODE_PRIVATE);
        return sharedPreferences.getString("TemperatureUnit", "C");
    }

    private String getWindSpeedUnit() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppSetting", MODE_PRIVATE);
        return sharedPreferences.getString("WindSpeedUnit", "km/h");
    }

    private void convertTemperatureValues(String newUnit) {
        SharedPreferences sharedPreferences = getSharedPreferences("WeatherAppPrefs", MODE_PRIVATE);
        float highTemp = sharedPreferences.getFloat(Constants.KEY_HIGH_TEMP, 0.0f);
        float lowTemp = sharedPreferences.getFloat(Constants.KEY_LOW_TEMP, 0.0f);
        float currentTemp = sharedPreferences.getFloat("CurrentTemp", 0.0f);

        if (newUnit.equals("F")) {
            highTemp = (highTemp * 9 / 5) + 32;
            lowTemp = (lowTemp * 9 / 5) + 32;
            currentTemp = (currentTemp * 9 / 5) + 32;
        } else {
            highTemp = (highTemp - 32) * 5 / 9;
            lowTemp = (lowTemp - 32) * 5 / 9;
            currentTemp = (currentTemp - 32) * 5 / 9;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(Constants.KEY_HIGH_TEMP, highTemp);
        editor.putFloat(Constants.KEY_LOW_TEMP, lowTemp);
        editor.putFloat("CurrentTemp", currentTemp);
        editor.apply();
    }

    private void convertWindSpeedValues(String newUnit) {
        // Access wind speed value stored in WeatherAppPrefs
        SharedPreferences sharedPreferences = getSharedPreferences("WeatherAppPrefs", MODE_PRIVATE);
        float windSpeed = sharedPreferences.getFloat("WindSpeed", 0.0f);

        // If no wind speed is stored, there's nothing to convert.
        if (windSpeed == 0.0f) {
            return;
        }

        if (newUnit.equals("m/s")) {
            windSpeed = windSpeed / 3.6f;
        } else { // newUnit is "km/h"
            windSpeed = windSpeed * 3.6f;
        }

        // Save the converted wind speed value back to SharedPreferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("WindSpeed", windSpeed);
        editor.apply();
    }

}