package com.appdevelopers.weatherapp;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class SideMenuDrawer extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView imageViewMenuBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_side_menu_drawer);
        drawerLayout = findViewById(R.id.main);
        imageViewMenuBar = findViewById(R.id.imageViewMenuBar);

        imageViewMenuBar.setOnClickListener(v -> {
            drawerLayout.open();
        });

    }
}