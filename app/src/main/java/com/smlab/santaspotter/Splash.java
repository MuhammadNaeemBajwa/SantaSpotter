package com.smlab.santaspotter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.smlab.santaspotter.databinding.ActivityAddSantaBinding;
import com.smlab.santaspotter.databinding.ActivitySplashBinding;

public class Splash extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private boolean userClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        new Handler().postDelayed(() -> {
//            Intent intent = new Intent(Splash.this, GetStarted.class);
//            startActivity(intent);
//            finish();
//        },3000);
//
//
//        binding.viewTouch.setOnClickListener(view -> startActivity(new Intent(Splash.this,GetStarted.class)));


        // Post delayed action
        new Handler().postDelayed(() -> {
            // Check if the user has already clicked
            if (!userClicked) {
                moveToNextActivity();
            }
        }, 3000);

        // Handle click on the splash screen view
        binding.viewTouch.setOnClickListener(view -> {
            userClicked = true;
            moveToNextActivity();
        });
    }

    // Method to move to the next activity
    private void moveToNextActivity() {
        Intent intent = new Intent(Splash.this, GetStarted.class);
        startActivity(intent);
        finish();
    }

}
