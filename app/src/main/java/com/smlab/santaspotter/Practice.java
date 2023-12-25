package com.smlab.santaspotter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Practice extends AppCompatActivity {
    private LottieAnimationView lottieAnimationView;

    private FrameLayout frameContainer;
    private View indicatorView;
    private BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice2);
        frameContainer = findViewById(R.id.frame_container);
        indicatorView = findViewById(R.id.indicator_view);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // Set initial fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, new BlankFragment())
                .commit();

        // Handle bottom navigation item clicks
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_container, new BlankFragment())
                                .commit();
                        updateIndicatorPosition(0);
                        return true;
                    case R.id.menu_search:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_container, new BlankFragment())
                                .commit();
                        updateIndicatorPosition(1);
                        return true;
                    case R.id.menu_profile:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_container, new BlankFragment())
                                .commit();
                        updateIndicatorPosition(2);
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    private void updateIndicatorPosition(int position) {
        float translationX = position * bottomNavigation.getWidth() / 3f;
        indicatorView.animate().translationX(translationX).setDuration(300).start();
    }
}