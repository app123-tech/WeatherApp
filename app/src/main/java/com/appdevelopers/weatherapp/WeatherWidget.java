package com.appdevelopers.weatherapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;

public class WeatherWidget extends AppWidgetProvider {

   static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        SharedPreferences widgetPrefs = context.getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE);

        String city = widgetPrefs.getString("City", "Unknown");
        String temp = widgetPrefs.getString("Temp", "0°C");
        String highTemp = widgetPrefs.getString("HighTemp", "H: --°C");
        String lowTemp = widgetPrefs.getString("LowTemp", "L: --°C");
        String iconUrl = widgetPrefs.getString("WeatherIcon", "");
        String feelsLike = widgetPrefs.getString("FeelsLike", "Unknown");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        views.setTextViewText(R.id.textViewCityName, city);
        views.setTextViewText(R.id.textViewTemp, temp);
        views.setTextViewText(R.id.textViewHighTemp, highTemp);
        views.setTextViewText(R.id.textViewLowTemp, lowTemp);
        views.setTextViewText(R.id.textViewFeelsLike, feelsLike);

        // Open the main activity when the widget is clicked
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

        if (!iconUrl.isEmpty()) {
            AppWidgetTarget appWidgetTarget = new AppWidgetTarget(context, R.id.imageViewIcon, views, appWidgetId);
            Glide.with(context.getApplicationContext()).asBitmap().load(iconUrl).into(appWidgetTarget);
        }

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent serviceIntent = new Intent(context, WeatherUpdateService.class);
        context.startService(serviceIntent);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        // Handle widget update broadcasts
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, WeatherWidget.class));
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}