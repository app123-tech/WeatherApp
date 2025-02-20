package com.appdevelopers.weatherapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

public class SplashScreenActivity extends AppCompatActivity {
    private ActivityResultLauncher<IntentSenderRequest> activityResultLauncher;
    private boolean isUpdateRequired = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Log.d(TAG, "Update flow completed successfully!");
                Toast.makeText(this, "App updated successfully", Toast.LENGTH_SHORT).show();
                proceedToNextActivity();
            } else {
                Log.e(TAG, "Update flow failed! Result code: " + result.getResultCode());
                Toast.makeText(this, "App update failed! You cannot proceed without updating", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        CheckForAppUpdate();

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this, GetStartedActivity.class);
            startActivity(intent);
            finish();
        }, 1000);
    }

    private void CheckForAppUpdate() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            Log.d(TAG, "Update Available: " + appUpdateInfo.updateAvailability());
            Log.d(TAG, "Is immediate update allowed: " + appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE));
            Log.d(TAG, "Is flexible update allowed: " + appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE));

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    Log.d(TAG, "Immediate update available");
                    Toast.makeText(this, "Immediate update is available. Please update the app", Toast.LENGTH_SHORT).show();
                    isUpdateRequired = true;

                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo,
                                activityResultLauncher,
                                AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build());
                    } catch (Exception e) {
                        Log.e(TAG, "Error starting update flow: " + e.getMessage());
                        Toast.makeText(this, "Failed to start update flow", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Log.d(TAG, "Update available but not allowed for immediate update");
                }
            } else {
                Log.d(TAG, "No update available");
                proceedToNextActivity();
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Failed to check for update: ", e);
            proceedToNextActivity();
        });
    }

    private void proceedToNextActivity() {
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}