package com.appdevelopers.weatherapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;

public class NoInternetActivity extends AppCompatActivity {

    private Button buttonTryAgain;
    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_no_internet);

        buttonTryAgain = findViewById(R.id.buttonTryAgain);
       lottieAnimationView = findViewById(R.id.lottieLoader);

        buttonTryAgain.setOnClickListener(v -> {
           lottieAnimationView.setVisibility(View.VISIBLE);
            lottieAnimationView.playAnimation();
            buttonTryAgain.setAlpha(0f);

            if (NetworkUtils.isConnected(NoInternetActivity.this)) {
                // If connected, navigate to MainActivity
                Intent intent = new Intent(NoInternetActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // If no internet, stop animation and show button again
                lottieAnimationView.cancelAnimation();
                lottieAnimationView.setVisibility(LottieAnimationView.GONE);
                buttonTryAgain.setAlpha(1f);
            }
        });
    }
}