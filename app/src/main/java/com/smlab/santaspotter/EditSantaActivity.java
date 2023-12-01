package com.smlab.santaspotter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.CompactDecimalFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.smlab.santaspotter.baseclasses.BaseActivity;
import com.smlab.santaspotter.databinding.ActivityMainBinding;
import com.smlab.santaspotter.fragments.EraserFragment;
import com.smlab.santaspotter.fragments.EraserVM;

public class EditSantaActivity extends BaseActivity implements EraserFragment.Listener {
    private static final String TAG = "EditSantaActivity";
    private ActivityMainBinding binding;
    Bitmap bitmap;
    Bitmap combinedBitmap;
    EraserVM eraserVM;

//    private int currentBrightnessProgress = 50;
//    private int currentTemperatureProgress = 50;
//    private int currentEraserSizeProgress = 50;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d(TAG, "onCreate: Coupon Code: " + getCouponCode());
        getIntentData();
        initialize();
        setListener();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        String combinedImagePath = intent.getStringExtra("combinedImagePath");
        int selectedStickerResId = intent.getIntExtra("selectedStickerDrawable", -1);
        Log.d(TAG, "getIntentData: " + selectedStickerResId);
        Log.d(TAG, "getIntentData: " + combinedImagePath);
        if (selectedStickerResId != -1) {
            bitmap = BitmapFactory.decodeResource(getResources(), selectedStickerResId);
        }
        if (combinedImagePath != null) {
            combinedBitmap = BitmapFactory.decodeFile(combinedImagePath);
        }

    }

    private void initialize() {
        eraserVM = new ViewModelProvider(this).get(EraserVM.class);
        getSupportFragmentManager().beginTransaction().add(R.id.eraserContainer, new EraserFragment(this, bitmap)).commit();
        setData();
    }

    private void setData() {
        binding.stickerView.addSticker(bitmap);
        binding.imgReceived.setImageBitmap(combinedBitmap);


        binding.stickerView.addSticker(bitmap);
//            binding.stickerView.setStickerBrightness(currentBrightnessProgress - 100);
//            binding.stickerView.setStickerTemperature(currentTemperatureProgress - 100);
//            eraserVM.getEraserSize().setValue((float) currentEraserSizeProgress);
//            binding.includeSantaStickers.seekBarBrightness.setProgress(currentBrightnessProgress);
//            binding.includeSantaStickers.seekBarTemperature.setProgress(currentTemperatureProgress);
//            binding.includeSantaStickers.seekBarEraser.setProgress(currentEraserSizeProgress);

    }

    private void setListener() {

        binding.includeSantaStickers.constriantImageFlip.setOnClickListener(view -> {
            binding.stickerView.flipSticker();
        });

        binding.includeSantaStickers.constraintImageBrithness.setOnClickListener(view -> {
            binding.includeSantaStickers.seekBarBrightness.setVisibility(View.VISIBLE);
            binding.includeSantaStickers.seekBarTemperature.setVisibility(View.GONE);
            binding.includeSantaStickers.seekBarEraser.setVisibility(View.GONE);

            binding.includeSantaStickers.titleEditSanta.setVisibility(View.GONE);

            binding.includeSantaStickers.selectedItemTitle.setVisibility(View.VISIBLE);

            binding.includeSantaStickers.selectedItemTitle.setText(R.string.brightness);


            seekBarBrightnessListener();
//
        });

        binding.includeSantaStickers.constraintImageTemprature.setOnClickListener(view -> {
            binding.includeSantaStickers.selectedItemTitle.setText(R.string.temperature);

            binding.includeSantaStickers.seekBarTemperature.setVisibility(View.VISIBLE);
            binding.includeSantaStickers.seekBarBrightness.setVisibility(View.GONE);
            binding.includeSantaStickers.seekBarEraser.setVisibility(View.GONE);

            binding.includeSantaStickers.selectedItemTitle.setVisibility(View.VISIBLE);
            binding.includeSantaStickers.titleEditSanta.setVisibility(View.GONE);

            binding.includeSantaStickers.selectedItemTitle.setText(R.string.temperature);
            seekBarTemperatureListener();

        });


        binding.includeSantaStickers.constraintImageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onSavedButton();

            }
        });

        binding.includeSantaStickers.constriantImageEraser.setOnClickListener(view -> {

            binding.includeSantaStickers.selectedItemTitle.setText(R.string.eraser);

            binding.includeSantaStickers.seekBarTemperature.setVisibility(View.GONE);
            binding.includeSantaStickers.seekBarBrightness.setVisibility(View.GONE);
            binding.includeSantaStickers.seekBarEraser.setVisibility(View.VISIBLE);

            binding.eraserContainer.setVisibility(View.VISIBLE);
            binding.stickerView.setVisibility(View.GONE);
            binding.includeSantaStickers.seekBarEraser.setVisibility(View.VISIBLE);


            binding.includeSantaStickers.titleEditSanta.setVisibility(View.GONE);

            binding.includeSantaStickers.selectedItemTitle.setVisibility(View.VISIBLE);
            binding.includeSantaStickers.selectedItemTitle.setText(R.string.eraser);
//            binding.includeSantaStickers.titleEditSanta.setTextColor(Color.RED);
//            binding.includeSantaStickers.titleEditSanta.setTextSize(14);

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

                binding.includeSantaStickers.selectedItemTitle.setVisibility(View.GONE);


            }
        });
    }

    private void seekBarTemperatureListener() {
        binding.includeSantaStickers.seekBarTemperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int updateProgress = progress - 100;
//                binding.stickerView.setStickerTemperature(updateProgress);
                binding.stickerView.setStickerTemperature(updateProgress);
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
                binding.includeSantaStickers.seekBarTemperature.setVisibility(View.GONE);
                binding.includeSantaStickers.textTemperatureValue.setVisibility(View.GONE);
                binding.includeSantaStickers.constraintTemperatureValue.setVisibility(View.GONE);
                binding.includeSantaStickers.imgTemperature.setVisibility(View.VISIBLE);

                binding.includeSantaStickers.titleEditSanta.setVisibility(View.VISIBLE);

                binding.includeSantaStickers.selectedItemTitle.setVisibility(View.GONE);

            }
        });
    }

    private void seekBarEraserListener() {
        binding.includeSantaStickers.seekBarEraser.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                eraserVM.getEraserSize().setValue((float) progress);


                binding.includeSantaStickers.imgEraser.setVisibility(View.GONE);
                binding.includeSantaStickers.textEraserValue.setText("" + progress + "%");
                binding.includeSantaStickers.constraintEraserValue.setVisibility(View.VISIBLE);
                binding.includeSantaStickers.titleEditSanta.setVisibility(View.GONE);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                binding.includeSantaStickers.seekBarEraser.setVisibility(View.VISIBLE);
                binding.includeSantaStickers.textEraserValue.setVisibility(View.VISIBLE);
                binding.includeSantaStickers.imgEraser.setVisibility(View.GONE);

                binding.includeSantaStickers.titleEditSanta.setVisibility(View.GONE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

//                binding.includeSantaStickers.titleEditSanta.setVisibility(View.VISIBLE);
                binding.includeSantaStickers.seekBarEraser.setVisibility(View.GONE);
                binding.includeSantaStickers.textEraserValue.setVisibility(View.GONE);
                binding.includeSantaStickers.constraintEraserValue.setVisibility(View.GONE);
                binding.includeSantaStickers.imgEraser.setVisibility(View.VISIBLE);

                binding.includeSantaStickers.titleEditSanta.setVisibility(View.VISIBLE);

                binding.includeSantaStickers.selectedItemTitle.setVisibility(View.GONE);


            }
        });
    }

    @Override
    public void onSaveBitmap(Bitmap bitmap) {
        binding.eraserContainer.setVisibility(View.GONE);
        binding.stickerView.setVisibility(View.VISIBLE);
        binding.stickerView.addSticker(bitmap);
    }


    private void onSavedButton() {
//        currentBrightnessProgress = binding.includeSantaStickers.seekBarBrightness.getProgress();
//        currentTemperatureProgress = binding.includeSantaStickers.seekBarTemperature.getProgress();
//        currentEraserSizeProgress = binding.includeSantaStickers.seekBarEraser.getProgress();

        // Switch back to sticker mode
        binding.eraserContainer.setVisibility(View.GONE);
        binding.stickerView.setVisibility(View.VISIBLE);
        binding.stickerView.addSticker(bitmap);

        setData();
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