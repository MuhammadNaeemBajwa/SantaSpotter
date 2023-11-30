package com.smlab.santaspotter;
import static android.content.ContentValues.TAG;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import androidx.annotation.Nullable;
import java.io.IOException;
public class UploadPhoto extends BaseActivity {
    Button btnCamera, btnGallery;
    String encodedImageData = "";
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
        getSupportActionBar().hide();

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
            pickImage(UploadPhoto.this);
        });

        btnGallery.setOnClickListener(v -> {
            pickImage(UploadPhoto.this);
        });
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
                } catch (IOException e) {
                    Log.d(TAG, "onActivityResult: IOException: " + e.getMessage());
                }
            }
        }
    }
}