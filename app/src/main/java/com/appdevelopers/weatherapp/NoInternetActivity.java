package com.appdevelopers.weatherapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NoInternetActivity extends AppCompatActivity {

    private Button buttonTryAgain;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_no_internet);

        buttonTryAgain = findViewById(R.id.buttonTryAgain);
        progressBar = findViewById(R.id.progressBar);

        buttonTryAgain.setOnClickListener(v -> {
            // Show the progress bar and hide the button with animations
            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(progressBar, "alpha", 0f, 1f);
            fadeIn.setDuration(300);
            fadeIn.start();

            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(buttonTryAgain, "alpha", 1f, 0f);
            fadeOut.setDuration(300);
            fadeOut.start();

            // Wait for the animation to finish before checking network connection
            fadeOut.addListener(new android.animation.Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(android.animation.Animator animation) {}

                @Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    // Check network connection after fade-out animation
                    if (NetworkUtils.isConnected(NoInternetActivity.this)) {
                        // If connected, start MainActivity
                        Intent intent = new Intent(NoInternetActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // If still not connected, hide progress bar and show button again with fade-in animation
                        ObjectAnimator fadeInButton = ObjectAnimator.ofFloat(buttonTryAgain, "alpha", 0f, 1f);
                        fadeInButton.setDuration(300);
                        fadeInButton.start();

                        ObjectAnimator fadeOutProgressBar = ObjectAnimator.ofFloat(progressBar, "alpha", 1f, 0f);
                        fadeOutProgressBar.setDuration(300);
                        fadeOutProgressBar.start();
                    }
                }

                @Override
                public void onAnimationCancel(android.animation.Animator animation) {}

                @Override
                public void onAnimationRepeat(android.animation.Animator animation) {}
            });
        });
    }
}