package com.smlab.santaspotter;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.smlab.santaspotter.databinding.ActivityAddSantaBinding;

import java.io.IOException;

public class AddSantaActivity extends BaseActivity {
    private static final int SELECT_SANTA_REQUEST = 54;
    StickerView stickerView;
    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;
    String encodedImageData = "";
    Uri uri;
    Dialog dialogCode;

    private ActivityAddSantaBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSantaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setIds();
        imageSet();
        setListener();
    }

    private void setIds() {
        stickerView = findViewById(R.id.stickerView);
    }

    private void imageSet() {

        String imagePath = getIntent().getStringExtra("imagePath");
        // Load the image into the ImageView using your loadProfile method
        loadProfile(this, imagePath, binding.imgReceived);
    }

    private void showCustomDialog() {
        dialogCode = new Dialog(this);
        dialogCode.setContentView(R.layout.adjust_sticker_dialog);
        dialogCode.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogCode.show();
    }

    private void setListener() {

//        binding.constraintPickSanta.setOnClickListener(view -> startActivity(new Intent(AddSantaActivity.this, SelectSanta.class)));
        binding.constraintPickSanta.setOnClickListener(view -> {
            Intent selectSantaIntent = new Intent(AddSantaActivity.this, SelectSanta.class);
            startActivityForResult(selectSantaIntent, SELECT_SANTA_REQUEST);
        });

        binding.imageBack.setOnClickListener(view -> {
//            onBackPressed();
            finish();
        });
        binding.btnCapturedImage.setOnClickListener(v -> {
            pickImage(AddSantaActivity.this);

        });

        binding.constraintUploadGallery.setOnClickListener(v -> {
            pickImage(AddSantaActivity.this);

        });
        binding.constraintShare.setOnClickListener(view -> {
            shareImage();
        });

    }


    private void shareImage() {
        // Replace "Your image content URI" with the actual URI of your image
        Uri imageUri = Uri.parse("Your image content URI");

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }

    private boolean isImageFromGallery(Intent intent) {
        // Check if the intent has extra information indicating that the image is from the gallery
        return intent.hasExtra("fromGallery") && intent.getBooleanExtra("fromGallery", false);
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        if (requestCode == AppConstants.REQUEST_CODE_For_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                uri = data.getParcelableExtra("path");
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    encodedImageData = getEncoded64ImageStringFromBitmap(bitmap);
                    Log.d(TAG, "onActivityResult: encodedImageData: " + encodedImageData);

                    loadProfile(AddSantaActivity.this, uri.toString(), binding.imgReceived);
                } catch (IOException e) {
                    Log.d(TAG, "onActivityResult: IOException: " + e.getMessage());
                }
            }
        }
    }

}