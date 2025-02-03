package com.appdevelopers.weatherapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.appdevelopers.weatherapp.Fragment.FragmentTenDays;
import com.appdevelopers.weatherapp.Fragment.FragmentTodayActivity;
import com.appdevelopers.weatherapp.Fragment.FragmentTomorrow;

public class MainActivity extends AppCompatActivity {
    private Button buttonToday, buttonTomorrow, buttonTenDays;
    private ImageView imageViewMenuBar;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        buttonToday = findViewById(R.id.buttonToday);
        buttonTomorrow = findViewById(R.id.buttonTomorrow);
        buttonTenDays = findViewById(R.id.buttonTenDays);
        imageViewMenuBar = findViewById(R.id.imageViewMenuBar);

        buttonToday.setOnClickListener(v -> {
            loadFragment(new FragmentTodayActivity());
        });

        buttonTomorrow.setOnClickListener(v -> {
            loadFragment(new FragmentTomorrow());
        });

        buttonTenDays.setOnClickListener(v -> {
            loadFragment(new FragmentTenDays());
        });

        imageViewMenuBar.setOnClickListener(v -> {
            drawerLayout.open();
        });
    }

    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}