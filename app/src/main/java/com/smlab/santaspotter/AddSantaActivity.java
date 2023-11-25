package com.smlab.santaspotter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class AddSantaActivity extends AppCompatActivity {

    ImageView imgReceivedFromUploadPhoto;
    Button btnCaptureImage, btnGalleryImage,btnSantaCap;
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
    }

    private void imageSet() {
        Intent intent = getIntent();
        uri = intent.getParcelableExtra("img");
        imgReceivedFromUploadPhoto.setImageURI(uri);

        // Check if the image is received from the gallery
        if (isImageFromGallery(intent)) {
            // Perform additional actions specific to images from the gallery
            // For example, update UI or show a message
//            Toast.makeText(this, "Image selected from the gallery", Toast.LENGTH_SHORT).show();
        }else {

        }

        // 04/09/2023 Use the receivedBitmap as needed in your CompanyName activity
        Bitmap receivedBitmap = getIntent().getParcelableExtra("imageBitmap");
        if (receivedBitmap != null) {
            imgReceivedFromUploadPhoto.setImageBitmap(receivedBitmap);
        } else {

        }

        btnSantaCap.setOnClickListener(view -> startActivity(new Intent(AddSantaActivity.this,SelectSanta.class)));
    }

    private void setListener() {

        btnCaptureImage.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        });

        btnGalleryImage.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_REQUEST);
        });

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
}