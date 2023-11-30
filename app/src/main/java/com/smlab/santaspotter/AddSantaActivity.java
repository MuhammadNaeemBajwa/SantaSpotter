package com.smlab.santaspotter;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smlab.santaspotter.databinding.ActivityAddSantaBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddSantaActivity extends BaseActivity {

    ImageView imgReceivedFromUploadPhoto, share;
    TextView backgroundTitle;
    String encodedImageData = "";
    Button btnCaptureImage, btnGalleryImage, btnSantaCap;
    StickerView stickerView;
    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;


    Uri uri;
    private ActivityAddSantaBinding binding;
    Dialog dialogCode;

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
        loadProfile(this, imagePath, imgReceivedFromUploadPhoto);


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
            onBackPressed();
        });
        binding.btnCapturedImage.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        });

        binding.constraintUploadGallery.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_REQUEST);
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
//                    encodedImageData = getEncoded64ImageStringFromBitmap(bitmap);
//                    Log.d(TAG, "onActivityResult: encodedImageData: " + encodedImageData);
//                    loadProfile(AddSantaActivity.this, uri.toString(), imgReceivedFromUploadPhoto);
//                    changePicture.setText(getResources().getString(R.string.changePicture));

                    int selectedStickerResId = data.getIntExtra("selectedSticker", -1);

                    if (selectedStickerResId != -1) {
                        Drawable selectedStickerDrawable = getResources().getDrawable(selectedStickerResId);
                        Bitmap combinedBitmap = Bitmap.createBitmap(existingImage.getWidth(), existingImage.getHeight(), existingImage.getConfig());
                        Canvas canvas = new Canvas(combinedBitmap);
                        canvas.drawBitmap(existingImage, new Matrix(), null);
                        // Add the selected sticker to the StickerView
                        binding.stickerView.addSticker(selectedStickerDrawable);

//                  Nov 29, 2023    -   On touch of sticker then dialog permission dialog show .
//                  After dismiss the dialog, 0.5sec delay  the moved towards the edit activity

                        binding.stickerView.setStickerTouchListener(() -> {
                            showCustomDialog();

                            new Handler().postDelayed(() -> {
                                Intent nextActivityIntent = new Intent(AddSantaActivity.this, EditSantaActivity.class);
                                String combinedImagePath = saveBitmapToFile(combinedBitmap);
                                nextActivityIntent.putExtra("combinedImagePath", combinedImagePath);
                                nextActivityIntent.putExtra("selectedStickerDrawable", selectedStickerResId);

                                startActivity(nextActivityIntent);
                            }, 500);
                        });


                        // Pass both the image and sticker to the next activity
//                    Intent nextActivityIntent = new Intent(AddSantaActivity.this, EditSantaActivity.class);
//
//                    // Save the combined bitmap to a file and pass the file path
//                    String combinedImagePath = saveBitmapToFile(combinedBitmap);
//                    nextActivityIntent.putExtra("combinedImagePath", combinedImagePath);
//                    // Pass the selected sticker drawable resource ID
//                    nextActivityIntent.putExtra("selectedStickerDrawable", selectedStickerResId);
//                    startActivity(nextActivityIntent);
//                    saveImageWithStickers();
                    } else {
                        // Handle result from the camera without a sticker
                        binding.imgReceived.setImageBitmap(photo);
                    }


                } catch (IOException e) {
                    Log.d(TAG, "onActivityResult: IOException: " + e.getMessage());
                }

//                else if (requestCode == PICK_REQUEST) {
//                    // Handle result from gallery
//                    Uri selectedImageUri = data.getData();
//
//                    // Set the selected image from the gallery as the background
//                    binding.imgReceived.setImageURI(selectedImageUri);
//                    int selectedStickerResId = data.getIntExtra("selectedSticker", -1);
//                    if (selectedStickerResId != -1) {
//                        // Add the selected sticker to the StickerView
//                        Drawable selectedStickerDrawable = getResources().getDrawable(selectedStickerResId);
//                        binding.stickerView.addSticker(selectedStickerDrawable);
//                    }
//                }
            }
        }
    }

}