package com.appdevelopers.weatherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("AppSetting", Context.MODE_PRIVATE);
            boolean isEnabled = sharedPreferences.getBoolean("DailyForecastNotification", false);
            if (isEnabled) {
                new Setting().scheduleDailyNotification();
            }
        }
    }
}
