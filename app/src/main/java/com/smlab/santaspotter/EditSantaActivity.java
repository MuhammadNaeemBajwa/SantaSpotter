package com.smlab.santaspotter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.CompactDecimalFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.smlab.santaspotter.databinding.ActivityMainBinding;
import com.smlab.santaspotter.fragments.EraserFragment;
import com.smlab.santaspotter.fragments.EraserVM;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Stack;

public class EditSantaActivity extends AppCompatActivity implements EraserFragment.Listener {
    private ActivityMainBinding binding;
    Bitmap bitmap;
    Bitmap combinedBitmap;
    EraserVM eraserVM;

    Bitmap rootViewBitmap;

    private boolean isImageSaved = false;

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

        binding.includeSantaStickers.btnUndo.setOnClickListener(view -> {
            shareImage();
        });

        binding.includeSantaStickers.constraintImageSave.setOnClickListener(view -> {
            saveImageIntoGallery();
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


    private void saveImageIntoGallery() {
//  Dec 1, 2023 -   Convert the root layout (binding.getRoot()) into a Bitmap
        if (rootViewBitmap!=null){
            rootViewBitmap = getBitmapFromView(binding.constraintLayout2);
            // Save the Bitmap to the gallery
            MediaStore.Images.Media.insertImage(
                    getContentResolver(),
                    rootViewBitmap,
                    "Santa Image",
                    "Hey! Your Santa Is Here."
            );
        }

    }

    private Bitmap getBitmapFromView(View view) {
        // Create a Bitmap with the same dimensions as the view
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        // Create a Canvas with the Bitmap
        Canvas canvas = new Canvas(bitmap);
        // Draw the view onto the Canvas
        view.draw(canvas);
        return bitmap;
    }

    private void shareImage() {
        if (rootViewBitmap!=null){
            rootViewBitmap = getBitmapFromView(binding.constraintLayout2);
            // Save the Bitmap to a temporary file
            File tempFile = saveBitmapToFile(rootViewBitmap);

            // Create an Intent to share the image using FileProvider
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");

            Uri contentUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", tempFile);
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this image from Santa App!");

            startActivity(Intent.createChooser(shareIntent, "Share Image"));
        }

    }


    private File saveBitmapToFile(Bitmap bitmap) {
        try {
            // Create a temporary file
            File tempFile = File.createTempFile("santa_image", ".png", getCacheDir());

            // Write the bitmap to the file
            FileOutputStream fos = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

}