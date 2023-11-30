package com.smlab.santaspotter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.smlab.santaspotter.databinding.ActivityMainBinding;
import com.smlab.santaspotter.fragments.EraserFragment;
import com.smlab.santaspotter.fragments.EraserVM;

public class EditSantaActivity extends AppCompatActivity implements EraserFragment.Listener {
    private ActivityMainBinding binding;
    Bitmap bitmap;
    Bitmap combinedBitmap;
    EraserVM eraserVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getIntentData();
        initialize();
        setListener();
    }

    private void initialize() {
        eraserVM = new ViewModelProvider(this).get(EraserVM.class);
        getSupportFragmentManager().beginTransaction().add(R.id.eraserContainer, new EraserFragment(this, bitmap)).commit();
        setData();
    }

    private void setData() {
        binding.stickerView.addSticker(bitmap);
        binding.imgReceived.setImageBitmap(combinedBitmap);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        String combinedImagePath = intent.getStringExtra("combinedImagePath");
        int selectedStickerResId = intent.getIntExtra("selectedStickerDrawable", -1);
        if (selectedStickerResId != -1) {
            bitmap = BitmapFactory.decodeResource(getResources(), selectedStickerResId);
        }
        if (combinedImagePath != null) {
            combinedBitmap = BitmapFactory.decodeFile(combinedImagePath);
        }

    }

    private void setListener() {

        binding.includeSantaStickers.constriantImageFlip.setOnClickListener(view -> {
            binding.stickerView.flipSticker();
        });

        binding.includeSantaStickers.constraintImageBrithness.setOnClickListener(view -> {
            binding.includeSantaStickers.seekBarBrightness.setVisibility(View.VISIBLE);
            binding.includeSantaStickers.seekBarTemperature.setVisibility(View.GONE);
            binding.includeSantaStickers.titleEditSanta.setText(R.string.brightness);
            binding.includeSantaStickers.titleEditSanta.setTextColor(Color.RED);
            binding.includeSantaStickers.titleEditSanta.setTextSize(12);
            seekBarBrightnessListener();
//
        });

        binding.includeSantaStickers.constraintImageTemprature.setOnClickListener(view -> {
            binding.includeSantaStickers.titleEditSanta.setText(R.string.brightness);
            binding.includeSantaStickers.titleEditSanta.setTextColor(Color.RED);
            binding.includeSantaStickers.titleEditSanta.setTextSize(12);
            binding.includeSantaStickers.seekBarTemperature.setVisibility(View.VISIBLE);
            binding.includeSantaStickers.seekBarBrightness.setVisibility(View.GONE);
            seekBarTemperatureListener();

        });


//        binding.includeSantaStickers.constraintImageSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////             saveImageWithStickers();
//            }
//        });

        binding.includeSantaStickers.constriantImageEraser.setOnClickListener(view -> {
            binding.eraserContainer.setVisibility(View.VISIBLE);
            binding.stickerView.setVisibility(View.GONE);
            binding.includeSantaStickers.seekBarEraser.setVisibility(View.VISIBLE);
            seekBarEraserListener();
        });
    }

    private void seekBarBrightnessListener() {
        binding.includeSantaStickers.seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

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

    private void seekBarTemperatureListener() {
        binding.includeSantaStickers.seekBarTemperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
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

    private void seekBarEraserListener() {
        binding.includeSantaStickers.seekBarEraser.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                eraserVM.getEraserSize().setValue((float) progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onSaveBitmap(Bitmap bitmap) {
        binding.eraserContainer.setVisibility(View.GONE);
        binding.stickerView.setVisibility(View.VISIBLE);
        binding.stickerView.addSticker(bitmap);
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