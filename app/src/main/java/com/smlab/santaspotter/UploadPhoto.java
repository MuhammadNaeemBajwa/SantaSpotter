package com.smlab.santaspotter;
import static android.content.ContentValues.TAG;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.smlab.santaspotter.baseclasses.BaseActivity;

import java.io.IOException;
public class UploadPhoto extends BaseActivity {
    ConstraintLayout btnCamera, btnGallery;
    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;
    private static final int CAMERA_PERMISSION_REQUEST = 123;
    String encodedImageData = "";
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        setIds();
        setListener();
    }

    private void setIds() {
        btnCamera = findViewById(R.id.open_camera_button);
        btnGallery = findViewById(R.id.upload_image_button);

    }

    private void setListener() {

        btnCamera.setOnClickListener(v -> {

            launchCamera();
//            pickImage(UploadPhoto.this);

        });

        btnGallery.setOnClickListener(v -> {

            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
            galleryIntent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
            galleryIntent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
//            startActivityForResult(galleryIntent, AppConstants.REQUEST_CODE_For_IMAGE);

            startActivityForResult(galleryIntent, PICK_REQUEST);
//            pickImage(UploadPhoto.this);
        });
    }

    private void launchCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        } else {
            // Permission already granted, launch camera intent
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // setting aspect ratio
            cameraIntent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
            cameraIntent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
            cameraIntent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

            // setting maximum bitmap width and height
            cameraIntent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
            cameraIntent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
            cameraIntent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }

    }

    //    Nov 30, 2023 -    For now avoiding the conflict.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            Bitmap img = (Bitmap) data.getExtras().get("data");
            if (img != null) {

                Intent iNext = new Intent(UploadPhoto.this, AddSantaActivity.class);
                iNext.putExtra("imageBitmap", img);

                startActivity(iNext);
            }
        } else if (requestCode == PICK_REQUEST && data != null) {
            // Get the selected image URI from the gallery
            Uri selectedImageUri = data.getData();

            // Start AddSantaActivity and pass the image URI, also indicate it's from the gallery
            Intent intent = new Intent(UploadPhoto.this, AddSantaActivity.class);
            intent.putExtra("img", selectedImageUri);
            intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
            startActivityForResult(intent, AppConstants.REQUEST_CODE_For_IMAGE);
            intent.putExtra("fromGallery", true);
            startActivity(intent);
        } else if (requestCode == AppConstants.REQUEST_CODE_For_IMAGE && resultCode == Activity.RESULT_OK) {
//            if (resultCode == Activity.RESULT_OK) {
            uri = data.getParcelableExtra("imagePath");
            if (uri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    encodedImageData = getEncoded64ImageStringFromBitmap(bitmap);
                    Log.d(TAG, "onActivityResult: encodedImageData: " + encodedImageData);
                } catch (IOException e) {
                    Log.d(TAG, "onActivityResult: IOException: " + e.getMessage());
                }
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, launch camera intent
                launchCamera();
            } else {
                // Permission denied, handle accordingly (show a message, etc.)
            }
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d(TAG, "onActivityResult: ");
//        if (requestCode == AppConstants.REQUEST_CODE_For_IMAGE && resultCode == Activity.RESULT_OK) {
////            if (resultCode == Activity.RESULT_OK) {
//            uri = data.getParcelableExtra("imagePath");
//            if (uri != null) {
//                try {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//                    encodedImageData = getEncoded64ImageStringFromBitmap(bitmap);
//                    Log.d(TAG, "onActivityResult: encodedImageData: " + encodedImageData);
//                } catch (IOException e) {
//                    Log.d(TAG, "onActivityResult: IOException: " + e.getMessage());
//                }
//            }
//
//        }
//    }
}