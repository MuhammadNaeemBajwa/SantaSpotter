//package com.smlab.santaspotter;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.widget.Button;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class UploadPhoto extends AppCompatActivity {
//
//    Button btnCamera, btnGallery;
//    private static final int CAMERA_REQUEST = 52;
//    private static final int PICK_REQUEST = 53;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_upload_photo);
//        getSupportActionBar().hide();
//
//        initialize();
//        setIds();
//        setListener();
//    }
//
//    private void initialize() {
//    }
//
//    private void setIds() {
//        btnCamera = findViewById(R.id.open_camera_button);
//        btnGallery = findViewById(R.id.upload_image_button);
//
//    }
//
//    private void setListener() {
//        btnCamera.setOnClickListener(v -> {
//            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(cameraIntent, CAMERA_REQUEST);
//
//        });
//
//        btnGallery.setOnClickListener(v -> {
//
//            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(galleryIntent, PICK_REQUEST);
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK&& data != null) {
//            Bitmap img = (Bitmap) data.getExtras().get("data");
//            if (img != null) {
//                Intent iNext = new Intent(UploadPhoto.this, AddSantaActivity.class);
//                iNext.putExtra("imageBitmap", img);
//                startActivity(iNext);
//            }
//        }
//
//        else if (requestCode == PICK_REQUEST && data != null) {
//            Uri selectedImageUri = data.getData();
//            Intent intent = new Intent(UploadPhoto.this, AddSantaActivity.class);
//            intent.putExtra("img", selectedImageUri);
//            intent.putExtra("fromGallery", true);
//            startActivity(intent);
//        }
//
//    }
//}