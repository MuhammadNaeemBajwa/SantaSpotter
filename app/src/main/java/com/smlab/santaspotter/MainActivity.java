package com.smlab.santaspotter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.widget.SeekBar;

import com.smlab.santaspotter.databinding.ActivityMainBinding;
import com.smlab.santaspotter.filter.ColorFilterGenerator;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        updateBrightnessColorFilter(binding.seekBar.getProgress());

        binding.text.setText(String.valueOf(binding.seekBar.getProgress()));


        // Set up a listener for the SeekBar changes
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

//                Nov 27, 2023  -   Both Brightness and Temperature Color Filter is perfectly fine.
                                  updateBrightnessColorFilter(progress);
//                updateTemperatureColorFilter(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }

    private void updateBrightnessColorFilter(int progress) {
        // Adjust the brightness based on the progress and set the color filter

//        Nov 27, 2023  -   Brightness functionality is perfectly working.

//
        binding.santaSticker.setColorFilter(
                ColorFilterGenerator.adjustBrightness(progress)
        );

    }

    private void updateTemperatureColorFilter(int progress) {

//        Nov 27, 2023  -   Temperature functionality is perfectly fine

        // Update the ColorFilter based on the temperature progress
        ColorFilter temperatureFilter = ColorFilterGenerator.adjustTemperatureUpdate(progress);

        // Apply the ColorFilter to your ImageView or any other view
        binding.santaSticker.setColorFilter(temperatureFilter);
    }

}