package com.smlab.santaspotter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class UploadPhoto extends AppCompatActivity {

    ConstraintLayout btnCamera;
            Button btnGallery;
    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);


        initialize();
        setIds();
        setListener();
    }

    private void initialize() {
    }

    private void setIds() {
        btnCamera = findViewById(R.id.open_camera_button);
        btnGallery = findViewById(R.id.upload_image_button);

    }

    private void setListener() {
        btnCamera.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);

        });

        btnGallery.setOnClickListener(v -> {

            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
////            data.getExtras().get("data")
////            Uri capturedImageUri = data.getData();
////            Intent intent = new Intent(UploadPhoto.this, AddSantaActivity.class);
////            intent.putExtra("img", capturedImageUri);
////            startActivity(intent);
//        }

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK&& data != null) {
            Bitmap img = (Bitmap) data.getExtras().get("data");
            if (img != null) {

                // Set the image to circleImageView
//                binding.circleImageView.setImageBitmap(img);
                Intent iNext = new Intent(UploadPhoto.this, AddSantaActivity.class);
                iNext.putExtra("imageBitmap", img);
                startActivity(iNext);
            }
        }

        else if (requestCode == PICK_REQUEST && data != null) {
            // Get the selected image URI from the gallery
            Uri selectedImageUri = data.getData();

            // Start AddSantaActivity and pass the image URI, also indicate it's from the gallery
            Intent intent = new Intent(UploadPhoto.this, AddSantaActivity.class);
            intent.putExtra("img", selectedImageUri);
            intent.putExtra("fromGallery", true);
            startActivity(intent);
        }

    }
}