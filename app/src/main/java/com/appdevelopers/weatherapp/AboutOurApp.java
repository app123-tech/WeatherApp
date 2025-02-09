package com.appdevelopers.weatherapp;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.internal.GsonBuildConfig;


public class AboutOurApp extends AppCompatActivity {
    private TextView textViewVersion, textViewLatestVersionInstallMessage;
    private ImageView imageViewBackArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about_our_app);

        textViewVersion = findViewById(R.id.textViewVersion);
        imageViewBackArrow = findViewById(R.id.imageViewBackArrow);
        textViewLatestVersionInstallMessage = findViewById(R.id.textViewLatestVersionInstallMessage);

        updateVersionNumber();

        imageViewBackArrow.setOnClickListener(v -> onBackPressed());
    }

    public void updateVersionNumber(){
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            textViewVersion.setText("Version " + versionName);

            String latestVersion = "1.0";
            if (versionName.equals(latestVersion)) {
                textViewLatestVersionInstallMessage.setText(R.string.the_latest_version_is_already_installed);
            } else {
                textViewLatestVersionInstallMessage.setText(R.string.a_new_version_is_available);
            }
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            textViewVersion.setText(R.string.version_info_not_available);
            textViewLatestVersionInstallMessage.setText(R.string.version_information_error);
        }
    }
}