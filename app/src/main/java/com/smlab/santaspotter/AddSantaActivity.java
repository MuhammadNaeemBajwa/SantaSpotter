package com.smlab.santaspotter;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class AddSantaActivity extends BaseActivity {

    ImageView imgReceivedFromUploadPhoto, share;
    TextView backgroundTitle;
    String encodedImageData = "";
    Button btnCaptureImage, btnGalleryImage, btnSantaCap;
    StickerView stickerView;
    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;


    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_santa);

        getSupportActionBar().hide();

        setIds();
        imageSet();
        setListener();
    }

    private void setIds() {
        imgReceivedFromUploadPhoto = findViewById(R.id.imgReceived);
        btnCaptureImage = findViewById(R.id.btnCapturedImage);
        btnGalleryImage = findViewById(R.id.btnUploadImageGallery);
        btnSantaCap = findViewById(R.id.santa_cap_button);
        share = findViewById(R.id.shareIcon);
        backgroundTitle = findViewById(R.id.textView_background);
        stickerView = findViewById(R.id.stickerView);
    }

    private void imageSet() {

        String imagePath = getIntent().getStringExtra("imagePath");

        // Load the image into the ImageView using your loadProfile method
        loadProfile(this, imagePath, imgReceivedFromUploadPhoto);


//        Intent intent = getIntent();
//        uri = intent.getParcelableExtra("img");
//        imgReceivedFromUploadPhoto.setImageURI(uri);
//
//        // Check if the image is received from the gallery
//        if (isImageFromGallery(intent)) {
//            // Perform additional actions specific to images from the gallery
//            // For example, update UI or show a message
////            Toast.makeText(this, "Image selected from the gallery", Toast.LENGTH_SHORT).show();
//        } else {
//
//        }
//
//        // 04/09/2023 Use the receivedBitmap as needed in your CompanyName activity
//        Bitmap receivedBitmap = getIntent().getParcelableExtra("imageBitmap");
//        if (receivedBitmap != null) {
//            imgReceivedFromUploadPhoto.setImageBitmap(receivedBitmap);
//        } else {
//
//        }

        btnSantaCap.setOnClickListener(view -> startActivity(new Intent(AddSantaActivity.this, SelectSanta.class)));
    }

    private void setListener() {

        btnCaptureImage.setOnClickListener(v -> {
//            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(cameraIntent, CAMERA_REQUEST);

            pickImage(AddSantaActivity.this);
        });

        btnGalleryImage.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_REQUEST);
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the share button click
                shareImage();
            }
        });
        backgroundTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stickerView.setVisibility(View.VISIBLE);
                // Add a sample sticker (you need to implement sticker adding logic)
                Drawable stickerDrawable = getResources().getDrawable(R.drawable.santa_sticker_1);
                stickerView.addSticker(stickerDrawable);
            }
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

                    loadProfile(AddSantaActivity.this, uri.toString(), imgReceivedFromUploadPhoto);
//                    changePicture.setText(getResources().getString(R.string.changePicture));
                } catch (IOException e) {
                    Log.d(TAG, "onActivityResult: IOException: " + e.getMessage());
                }
            }
        }
    }

}