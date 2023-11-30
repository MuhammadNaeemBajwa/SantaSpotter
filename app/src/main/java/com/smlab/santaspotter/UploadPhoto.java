package com.smlab.santaspotter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.smlab.santaspotter.filter.AppConstants;
import com.smlab.santaspotter.filter.BaseActivity;

import java.io.IOException;

public class UploadPhoto extends BaseActivity {
    ConstraintLayout btnCamera;
    Button btnGallery;
    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;

    String encodedImageData = "";
    Uri uri;

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
            // setting aspect ratio
            cameraIntent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
            cameraIntent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
            cameraIntent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

            // setting maximum bitmap width and height
            cameraIntent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
            cameraIntent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
            cameraIntent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

            startActivityForResult(cameraIntent, CAMERA_REQUEST);

//            pickImage(UploadPhoto.this);

        });

        btnGallery.setOnClickListener(v -> {

            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
            galleryIntent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
            galleryIntent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
            startActivityForResult(galleryIntent, AppConstants.REQUEST_CODE_For_IMAGE);

            startActivityForResult(galleryIntent, PICK_REQUEST);
//            pickImage(UploadPhoto.this);
        });
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
        }
        else if (requestCode == PICK_REQUEST && data != null) {
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
        }
        else if (requestCode == AppConstants.REQUEST_CODE_For_IMAGE && resultCode == Activity.RESULT_OK) {
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
