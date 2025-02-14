package com.appdevelopers.weatherapp;

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
}
