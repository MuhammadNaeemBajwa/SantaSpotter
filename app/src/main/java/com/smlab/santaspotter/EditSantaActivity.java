package com.smlab.santaspotter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.Toast;

import com.smlab.santaspotter.databinding.ActivityMainBinding;
import com.smlab.santaspotter.filter.ColorFilterGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditSantaActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int brightnessProgress = 0;
    private int temperatureProgress = 0;

    Drawable selectedStickerDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListener();
        receivedImage();
    }

    private void receivedImage() {
//        Intent intent = getIntent();
//        Bitmap combinedBitmap = intent.getParcelableExtra("combinedBitmap");
//        int selectedStickerResId = intent.getIntExtra("selectedStickerDrawable", -1);
//        if (combinedBitmap != null) {
//            // Set the combined image to the ImageView in the next activity
//            binding.imgReceived.setImageBitmap(combinedBitmap);
//
//            // Add the selected sticker to the StickerView (if needed)
//            if (selectedStickerResId != -1) {
//                 selectedStickerDrawable = getResources().getDrawable(selectedStickerResId);
//                binding.stickerView.addSticker(selectedStickerDrawable);
//            }
//
//        }

        Intent intent = getIntent();
        String combinedImagePath = intent.getStringExtra("combinedImagePath");
        int selectedStickerResId = intent.getIntExtra("selectedStickerDrawable", -1);

        if (combinedImagePath != null) {
            // Load the combined image from the file
            Bitmap combinedBitmap = BitmapFactory.decodeFile(combinedImagePath);

            // Set the combined image to the ImageView in the next activity
            binding.imgReceived.setImageBitmap(combinedBitmap);

            // Add the selected sticker to the StickerView (if needed)
            if (selectedStickerResId != -1) {
                Drawable selectedStickerDrawable = getResources().getDrawable(selectedStickerResId);
                binding.stickerView.addSticker(selectedStickerDrawable);
            }
        }

    }

    private void setListener() {

        binding.includeSantaStickers.constriantImageFlip.setOnClickListener(view -> {
            binding.stickerView.flipSticker();
        });

        binding.includeSantaStickers.constraintImageBrithness.setOnClickListener(view -> {
            binding.includeSantaStickers.seekBarBrightness.setVisibility(View.VISIBLE);
            binding.includeSantaStickers.seekBarTemperature.setVisibility(View.GONE);
//            updateBrightnessColorFilter(binding.includeSantaStickers.seekBarBrightness.getProgress());

            binding.includeSantaStickers.titleEditSanta.setText(R.string.brightness);
            binding.includeSantaStickers.titleEditSanta.setTextColor(Color.RED);
            binding.includeSantaStickers.titleEditSanta.setTextSize(12);
//            binding.includeSantaStickers.titleEditSanta.setVisibility(View.GONE);

            seekBarBrightnessListener();
//
        });

        binding.includeSantaStickers.constraintImageTemprature.setOnClickListener(view -> {

            binding.includeSantaStickers.titleEditSanta.setText(R.string.brightness);
            binding.includeSantaStickers.titleEditSanta.setTextColor(Color.RED);
            binding.includeSantaStickers.titleEditSanta.setTextSize(12);


            binding.includeSantaStickers.seekBarTemperature.setVisibility(View.VISIBLE);
            binding.includeSantaStickers.seekBarBrightness.setVisibility(View.GONE);
//            updateTemperatureColorFilter(binding.includeSantaStickers.seekBarTemperature.getProgress());
//            binding.includeSantaStickers.titleEditSanta.setVisibility(View.GONE);

            seekBarTemperatureListener();

        });


//        binding.includeSantaStickers.constraintImageSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////             saveImageWithStickers();
//            }
//        });
    }

    private void seekBarBrightnessListener(){
        binding.includeSantaStickers.seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

//                Nov 27, 2023  -   Both Brightness and Temperature Color Filter is perfectly fine.
                brightnessProgress = progress;
//                updateBrightnessColorFilter(progress);
                int updateProgress = progress - 100;
                binding.stickerView.setStickerBrightness(updateProgress);
//                binding.stickerView.updateBrightnessColorFilter(selectedStickerDrawable,updateProgress);

                binding.includeSantaStickers.imgBrightness.setVisibility(View.GONE);
                binding.includeSantaStickers.textBrightnessValue.setText("" + updateProgress + "%");
                binding.includeSantaStickers.contraintBrightnessValue.setVisibility(View.VISIBLE);

                binding.includeSantaStickers.titleEditSanta.setVisibility(View.GONE);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                binding.includeSantaStickers.seekBarBrightness.setVisibility(View.VISIBLE);
                binding.includeSantaStickers.textBrightnessValue.setVisibility(View.VISIBLE);
                binding.includeSantaStickers.imgBrightness.setVisibility(View.GONE);

                binding.includeSantaStickers.titleEditSanta.setVisibility(View.GONE);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                binding.includeSantaStickers.seekBarBrightness.setVisibility(View.GONE);
                binding.includeSantaStickers.textBrightnessValue.setVisibility(View.GONE);
                binding.includeSantaStickers.contraintBrightnessValue.setVisibility(View.GONE);
                binding.includeSantaStickers.imgBrightness.setVisibility(View.VISIBLE);

                binding.includeSantaStickers.titleEditSanta.setVisibility(View.VISIBLE);


            }
        });
    }

    private void seekBarTemperatureListener(){
        binding.includeSantaStickers.seekBarTemperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

//                Nov 27, 2023  -   Both Brightness and Temperature Color Filter is perfectly fine.
                temperatureProgress = progress;
//                updateTemperatureColorFilter(progress);
                int updateProgress = progress - 100;
//                binding.stickerView.setStickerTemperature(updateProgress);
                binding.stickerView.setStickerBrightness(updateProgress);
                binding.includeSantaStickers.imgTemperature.setVisibility(View.GONE);
                binding.includeSantaStickers.textTemperatureValue.setText("" + updateProgress + "%");
                binding.includeSantaStickers.constraintTemperatureValue.setVisibility(View.VISIBLE);

                binding.includeSantaStickers.titleEditSanta.setVisibility(View.GONE);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                binding.includeSantaStickers.seekBarTemperature.setVisibility(View.VISIBLE);
                binding.includeSantaStickers.textTemperatureValue.setVisibility(View.VISIBLE);
                binding.includeSantaStickers.imgTemperature.setVisibility(View.GONE);

                binding.includeSantaStickers.titleEditSanta.setVisibility(View.GONE);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                binding.includeSantaStickers.seekBarTemperature.setVisibility(View.GONE);
                binding.includeSantaStickers.textTemperatureValue.setVisibility(View.GONE);
                binding.includeSantaStickers.constraintTemperatureValue.setVisibility(View.GONE);
                binding.includeSantaStickers.imgTemperature.setVisibility(View.VISIBLE);

                binding.includeSantaStickers.titleEditSanta.setVisibility(View.VISIBLE);

            }
        });
    }

//    private void updateBrightnessColorFilter(int progress) {
//        // Convert progress to a float value in the range [0.0, 2.0]
//        float brightnessFactor = (float) (progress + 100) / 100.0f;
//        Log.d(TAG, "updateBrightnessColorFilter: progress: " + progress);
//        Log.d(TAG, "updateBrightnessColorFilter: " + brightnessFactor);
//        // Adjust brightness using ColorMatrix
//        ColorMatrix colorMatrix = new ColorMatrix();
//        colorMatrix.set(new float[]{
//                brightnessFactor, 0, 0, 0, 0,
//                0, brightnessFactor, 0, 0, 0,
//                0, 0, brightnessFactor, 0, 0,
//                0, 0, 0, 1, 0
//        });
//
//        // Apply the ColorMatrix to the ColorFilter
//        ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
//
//        // Apply the ColorFilter to the ImageView
////        binding.santaSticker.setColorFilter(colorFilter);
//    }
//    private void updateTemperatureColorFilter(int progress) {
//
////        Nov 27, 2023  -   Temperature functionality is perfectly fine
//        ColorFilter temperatureFilter = ColorFilterGenerator.adjustTemperatureUpdate(progress);
//        // Apply the ColorFilter to your ImageView or any other view
////        binding.santaSticker.setColorFilter(temperatureFilter);
//    }

    //    private void updateBrightnessColorFilter(int progress) {
////        Nov 27, 2023  -   Brightness functionality is perfectly working.
//        binding.santaSticker.setColorFilter(
//                ColorFilterGenerator.adjustBrightness(progress)
//        );
//    }
}