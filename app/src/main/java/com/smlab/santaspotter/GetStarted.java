package com.smlab.santaspotter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class GetStarted extends AppCompatActivity {

    Animation zoom_in, bottomAnim;
    ConstraintLayout constraintLayout2;
    ImageView img_santa;
    Button button_spot_him;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);


        img_santa = findViewById(R.id.getStarted_logo);
        button_spot_him = findViewById(R.id.button_spot_him);
        constraintLayout2 = findViewById(R.id.started_constraint_2);

        button_spot_him.setOnClickListener(view -> {
            Intent intent = new Intent(GetStarted.this, UploadPhoto.class);
            startActivity(intent);
            finish();
        });

        zoom_in = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        constraintLayout2.setAnimation(bottomAnim);

//        zoom_in.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                img_santa.setScaleX(1.0f); // Set the desired final X scale
//                img_santa.setScaleY(1.0f); // Set the desired final Y scale
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                // Animation ended, set the final scale on ImageView
//                img_santa.setScaleX(3.0f); // Set the desired final X scale
//                img_santa.setScaleY(3.0f); // Set the desired final Y scale
//                animation.cancel();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
////animation.cancel();
//
//            }
//        });
//        img_santa.setAnimation(zoom_in);


        // Define the scale animation
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1.0f, // From X scale
                3.0f, // To X scale
                1.0f, // From Y scale
                3.0f, // To Y scale
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point X (center)
                Animation.RELATIVE_TO_SELF, 0.5f  // Pivot point Y (center)
        );

        scaleAnimation.setDuration(1200); // Set the duration in milliseconds
        scaleAnimation.setInterpolator(new DecelerateInterpolator()); // Set an interpolator for smoothness

        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Animation start actions if needed
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Animation end actions if needed
                img_santa.setScaleX(3.0f); // Set the desired final X scale
                img_santa.setScaleY(3.0f); // Set the desired final Y scale
                animation.cancel();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Animation repeat actions if needed
            }
        });

        img_santa.startAnimation(scaleAnimation); // Start the scale animation

    }
}