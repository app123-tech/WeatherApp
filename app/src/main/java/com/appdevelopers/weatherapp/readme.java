package com.appdevelopers.weatherapp;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Window;
import android.view.WindowManager;

public class readme {
//    package com.appdevelopers.weatherapp;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.graphics.drawable.GradientDrawable;
//import android.os.Bundle;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//
//    public class Setting extends AppCompatActivity {
//        private CardView cardView2, cardView3, cardView5;
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_setting);
//
//            cardView2 = findViewById(R.id.cardView2);
//            cardView3 = findViewById(R.id.cardView3);
//            cardView5 = findViewById(R.id.cardView5);
//
//            cardView2.setOnClickListener(v -> showUnitSelectionDialog("Temperature Unit"));
//            cardView3.setOnClickListener(v -> showUnitSelectionDialog("Wind Speed Unit"));
//            cardView5.setOnClickListener(v -> showLanguageSelectionDialog());
//        }
//
//        private void showUnitSelectionDialog(String title) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle(title);
//            builder.setView(R.layout.custom_unit_selection);
//            AlertDialog dialog = builder.create();
//
//            dialog.setOnShowListener(dialogInterface -> {
//                Window window = dialog.getWindow();
//                if (window != null) {
//                    window.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);
//                    GradientDrawable drawable = new GradientDrawable();
//                    drawable.setCornerRadius(60);
//                    drawable.setColor(Color.WHITE);
//                    window.setBackgroundDrawable(drawable);
//                }
//            });
//
//            dialog.show();
//        }
//
//        private void showLanguageSelectionDialog() {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            android.view.LayoutInflater inflater = getLayoutInflater();
//            android.view.View dialogView = inflater.inflate(R.layout.custom_multi_language_selection, null);
//            builder.setView(dialogView);
//
//            RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);
//            TextView textViewAccept = dialogView.findViewById(R.id.textViewAccept);
//            TextView textViewCancel = dialogView.findViewById(R.id.textViewCancel);
//
//            AlertDialog dialog = builder.create();
//
//            dialog.setOnShowListener(dialogInterface -> {
//                Window window = dialog.getWindow();
//                if (window != null) {
//                    window.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);
//                    GradientDrawable drawable = new GradientDrawable();
//                    drawable.setCornerRadius(60);
//                    drawable.setColor(Color.WHITE);
//                    window.setBackgroundDrawable(drawable);
//                }
//            });
//
//            SharedPreferences sharedPreferences = getSharedPreferences("AppSetting", MODE_PRIVATE);
//            String selectedLanguageCode = sharedPreferences.getString("Language", "en");
//
//            if (selectedLanguageCode.equals("en")) {
//                radioGroup.check(R.id.radioButtonEnglish);
//            } else if (selectedLanguageCode.equals("ne")) {
//                radioGroup.check(R.id.radioButtonNepali);
//            } else if (selectedLanguageCode.equals("it")) {
//                radioGroup.check(R.id.radioButtonItalian);
//            } else if (selectedLanguageCode.equals("ko")) {
//                radioGroup.check(R.id.radioButtonKorean);
//            }
//
//            textViewAccept.setOnClickListener(v1 -> {
//                int selectedId = radioGroup.getCheckedRadioButtonId();
//                String newLanguageCode = "en";
//
//                if (selectedId == R.id.radioButtonNepali) {
//                    newLanguageCode = "ne";
//                } else if (selectedId == R.id.radioButtonItalian) {
//                    newLanguageCode = "it";
//                } else if (selectedId == R.id.radioButtonKorean) {
//                    newLanguageCode = "ko";
//                }
//                setLocale(newLanguageCode);
//                dialog.dismiss();
//            });
//
//            textViewCancel.setOnClickListener(v1 -> dialog.dismiss());
//            dialog.show();
//        }
//
//        private void setLocale(String languageCode) {
//            SharedPreferences sharedPreferences = getSharedPreferences("AppSetting", MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("Language", languageCode);
//            editor.apply();
//
//            LocaleHelper.setLocale(this, languageCode);
//        }
//    }


    //old
//    cardView2.setOnClickListener(v -> {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        android.view.LayoutInflater inflater = getLayoutInflater();
//        android.view.View dialogView = inflater.inflate(R.layout.custom_temperature_unit_selection, null);
//        builder.setView(dialogView);
//
//        android.widget.RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);
//        android.widget.TextView textViewAccept = dialogView.findViewById(R.id.textViewAccept);
//        android.widget.TextView textViewCancel = dialogView.findViewById(R.id.textViewCancel);
//
//        AlertDialog dialog = builder.create();
//
//        SharedPreferences sharedPreferences = getSharedPreferences("AppSetting", MODE_PRIVATE);
//        String selectedUnit = sharedPreferences.getString("TemperatureUnit", "C"); // Default to Celsius
//
//        if (selectedUnit.equals("C")) {
//            radioGroup.check(R.id.radioButtonCelsius);
//        } else {
//            radioGroup.check(R.id.radioButtonFahrenheit);
//        }
//
//        textViewAccept.setOnClickListener(v1 -> {
//            int selectedId = radioGroup.getCheckedRadioButtonId();
//            String newUnit = (selectedId == R.id.radioButtonCelsius) ? "C" : "F";
//            editor.putString("TemperatureUnit", newUnit).apply();
//            dialog.dismiss();
//        });
//        textViewCancel.setOnClickListener(v1 -> dialog.dismiss());
//        dialog.show();
//    });
//
//cardView3.setOnClickListener(v -> {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        android.view.LayoutInflater inflater = getLayoutInflater();
//        android.view.View dialogView = inflater.inflate(R.layout.custom_wind_speed_unit_selection, null);
//        builder.setView(dialogView);
//
//        android.widget.RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);
//        android.widget.TextView textViewAccept = dialogView.findViewById(R.id.textViewAccept);
//        android.widget.TextView textViewCancel = dialogView.findViewById(R.id.textViewCancel);
//
//        AlertDialog dialog = builder.create();
//
//        SharedPreferences sharedPreferences = getSharedPreferences("AppSetting", MODE_PRIVATE);
//        String selectedUnit = sharedPreferences.getString("WindSpeedUnit", "km/h");
//
//        if (selectedUnit.equals("km/h")) {
//            radioGroup.check(R.id.radioButtonKilometers);
//        } else {
//            radioGroup.check(R.id.radioButtonMeters);
//        }
//
//        textViewAccept.setOnClickListener(v1 -> {
//            int selectedId = radioGroup.getCheckedRadioButtonId();
//            String newUnit = (selectedId == R.id.radioButtonKilometers) ? "km/h" : "m/h";
//            editor.putString("WindSpeedUnit", newUnit).apply();
//            dialog.dismiss();
//        });
//        textViewCancel.setOnClickListener(v1 -> dialog.dismiss());
//        dialog.show();
//    });
//
//cardView5.setOnClickListener(v -> {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        android.view.LayoutInflater inflater = getLayoutInflater();
//        android.view.View dialogView = inflater.inflate(R.layout.custom_multi_language_selection, null);
//        builder.setView(dialogView);
//
//        android.widget.RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);
//        android.widget.TextView textViewAccept = dialogView.findViewById(R.id.textViewAccept);
//        android.widget.TextView textViewCancel = dialogView.findViewById(R.id.textViewCancel);
//
//        AlertDialog dialog = builder.create();
//
//        SharedPreferences sharedPreferences = getSharedPreferences("AppSetting", MODE_PRIVATE);
//        String selectedLanguageCode = sharedPreferences.getString("Language", "en");
//
//        switch (selectedLanguageCode) {
//            case "ne":
//                radioGroup.check(R.id.radioButtonNepali);
//                break;
//            case "it":
//                radioGroup.check(R.id.radioButtonItalian);
//                break;
//            case "ko":
//                radioGroup.check(R.id.radioButtonKorean);
//                break;
//            default:
//                radioGroup.check(R.id.radioButtonEnglish);
//                break;
//        }
//
//        textViewAccept.setOnClickListener(v1 -> {
//            int selectedId = radioGroup.getCheckedRadioButtonId();
//            String newLanguageCode = "en";
//
//            if (selectedId == R.id.radioButtonNepali) {
//                newLanguageCode = "ne";
//            } else if (selectedId == R.id.radioButtonItalian) {
//                newLanguageCode = "it";
//            } else if (selectedId == R.id.radioButtonKorean) {
//                newLanguageCode = "ko";
//            }
//
//            setLocale(newLanguageCode);
//            dialog.dismiss();
//        });
//        textViewCancel.setOnClickListener(v1 -> dialog.dismiss());
//        dialog.show();
//    });


    // temperature selection
//    cardView2.setOnClickListener(v -> {
//        // Inflate the dialog layout
//        android.view.LayoutInflater inflater = getLayoutInflater();
//        android.view.View dialogView = inflater.inflate(R.layout.dialog_temperature_selection, null);
//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
//        builder.setView(dialogView);
//
//        // Initialize UI components in the dialog
//        android.widget.RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroupTemperature);
//        android.widget.RadioButton radioButtonCelsius = dialogView.findViewById(R.id.radioButtonCelsius);
//        android.widget.RadioButton radioButtonFahrenheit = dialogView.findViewById(R.id.radioButtonFahrenheit);
//        android.widget.TextView textViewAccept = dialogView.findViewById(R.id.textViewAccept);
//        android.widget.TextView textViewCancel = dialogView.findViewById(R.id.textViewCancel);
//
//        android.app.AlertDialog dialog = builder.create();
//
//        // Retrieve saved preference
//        SharedPreferences sharedPreferences = getSharedPreferences("AppSetting", MODE_PRIVATE);
//        String selectedUnit = sharedPreferences.getString("TemperatureUnit", "C"); // Default to Celsius
//
//        // Set the checked radio button based on saved preference
//        if (selectedUnit.equals("C")) {
//            radioButtonCelsius.setChecked(true);
//        } else {
//            radioButtonFahrenheit.setChecked(true);
//        }
//
//        textViewAccept.setOnClickListener(v1 -> {
//            // Get selected unit
//            int selectedId = radioGroup.getCheckedRadioButtonId();
//            String newUnit = "C"; // Default to Celsius
//
//            if (selectedId == R.id.radioButtonFahrenheit) {
//                newUnit = "F";
//            }
//
//            // Save the new unit in SharedPreferences
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("TemperatureUnit", newUnit);
//            editor.apply();
//
//            // Update the UI
//            textViewTemperature.setText(newUnit.equals("C") ? "°C" : "°F");
//
//            dialog.dismiss();
//        });
//
//        textViewCancel.setOnClickListener(v1 -> dialog.dismiss());
//
//        // Show the dialog
//        dialog.show();
//    });


//    SharedPreferences sharedPreferences = getSharedPreferences("AppSetting", MODE_PRIVATE);
//    String temperatureUnit = sharedPreferences.getString("TemperatureUnit", "C"); // Default °C
//textViewTemperature.setText(temperatureUnit.equals("C") ? "°C" : "°F");

//    AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
//            dialog.setOnShowListener(dialogInterface -> {
//        Window window = dialog.getWindow();
//        if (window != null) {
//            window.setLayout( int)
//            (getResources().getDisplayMetrics().widthPixels * 0.90), WindowManager.LayoutParams.WRAP_CONTENT)
//            ;
//            GradientDrawable drawable = new GradientDrawable();
//            drawable.setCornerRadius(60);
//            drawable.setColor(Color.WHITE);
//
//            window.setBackgroundDrawable(drawable);
//        }
//    });
//    //AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
//    android.view.LayoutInflater inflater = getLayoutInflater();
//    android.view.View dialogView = inflater.inflate(R.layout.custom_multi_language_selection, null);
//            builder.setView(dialogView);
//
//    android.widget.RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);
//    android.widget.TextView textViewAccept = dialogView.findViewById(R.id.textViewAccept);
//    android.widget.TextView textViewCancel = dialogView.findViewById(R.id.textViewCancel);
//
//    android.app.AlertDialog dialog = builder.create();
//
//    SharedPreferences sharedPreferences = getSharedPreferences("AppSetting", MODE_PRIVATE);
//    String selectedLanguageCode = sharedPreferences.getString("Language", "en"); // Default to "en"
//
//    // Check the corresponding radio button based on the stored language code
//            if (selectedLanguageCode.equals("en")) {
//        radioGroup.check(R.id.radioButtonEnglish);
//    } else if (selectedLanguageCode.equals("ne")) {
//        radioGroup.check(R.id.radioButtonNepali);
//    } else if (selectedLanguageCode.equals("it")) {
//        radioGroup.check(R.id.radioButtonItalian);
//    } else if (selectedLanguageCode.equals("ko")) {
//        radioGroup.check(R.id.radioButtonKorean);
//    }
//
//            textViewAccept.setOnClickListener(v1 -> {
//        int selectedId = radioGroup.getCheckedRadioButtonId();
//        String newLanguageCode = "en"; // Default to English
//
//        if (selectedId == R.id.radioButtonNepali) {
//            newLanguageCode = "ne";
//        } else if (selectedId == R.id.radioButtonItalian) {
//            newLanguageCode = "it";
//        } else if (selectedId == R.id.radioButtonKorean) {
//            newLanguageCode = "ko";
//        }
//        setLocale(newLanguageCode);
//        dialog.dismiss();
//    });
//            textViewCancel.setOnClickListener(v1 -> dialog.dismiss());
//            dialog.show();
}
