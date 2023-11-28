package com.smlab.santaspotter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smlab.santaspotter.databinding.ActivityAddSantaBinding;

import java.io.IOException;

public class AddSantaActivity extends AppCompatActivity {
    //    ImageView imgReceivedFromUploadPhoto, share;
    TextView backgroundTitle;
    //    ConstraintLayout btnCaptureImage, btnGalleryImage, btnSantaCap;
    StickerView stickerView;
    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;
    Uri uri;
    String scannedResult ="";

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
//        imgReceivedFromUploadPhoto = findViewById(R.id.imgReceived);
//        btnCaptureImage = findViewById(R.id.constraintCameraCapture);
//        btnGalleryImage = findViewById(R.id.constraintUploadGallery);
//        btnSantaCap = findViewById(R.id.constraintPickSanta);
//        share = findViewById(R.id.shareIcon);
//        backgroundTitle = findViewById(R.id.textView_background);
        stickerView = findViewById(R.id.stickerView);
    }

    private void imageSet() {
        Intent intent = getIntent();
        uri = intent.getParcelableExtra("img");
//        imgReceivedFromUploadPhoto.setImageURI(uri);
        binding.imgReceived.setImageURI(uri);

        if (isImageFromGallery(intent)) {
            // Perform additional actions specific to images from the gallery
            // For example, update UI or show a message
//            Toast.makeText(this, "Image selected from the gallery", Toast.LENGTH_SHORT).show();
        } else {

        }

        // 04/09/2023 Use the receivedBitmap as needed in your CompanyName activity
        Bitmap receivedBitmap = getIntent().getParcelableExtra("imageBitmap");
        if (receivedBitmap != null) {
//            imgReceivedFromUploadPhoto.setImageBitmap(receivedBitmap);
            binding.imgReceived.setImageBitmap(receivedBitmap);
        } else {

        }

//        btnSantaCap.setOnClickListener(view -> startActivity(new Intent(AddSantaActivity.this, SelectSanta.class)));
        binding.constraintPickSanta.setOnClickListener(view -> startActivity(new Intent(AddSantaActivity.this, SelectSanta.class)));
    }

    private void setListener() {

        binding.imageBack.setOnClickListener(view -> {
            onBackPressed();
        });

//        btnCaptureImage.setOnClickListener(v -> {
        binding.btnCapturedImage.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        });

//        btnGalleryImage.setOnClickListener(v -> {
        binding.constraintUploadGallery.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_REQUEST);
        });
//        share.setOnClickListener(view -> {
        binding.constraintShare.setOnClickListener(view -> {
//            Intent shareIntent = new Intent(Intent.ACTION_SEND);
//            shareIntent.setType("image/plain");
//            shareIntent.putExtra(Intent.EXTRA_TEXT, "scannedResult");
//            startActivity(Intent.createChooser(shareIntent, "Share Link"));

            shareImage();
        });
//        backgroundTitle.setOnClickListener(view -> {
        binding.textViewBackground.setOnClickListener(view -> {
//            stickerView.setVisibility(View.VISIBLE);
            binding.stickerView.setVisibility(View.VISIBLE);
            // Add a sample sticker (you need to implement sticker adding logic)
            Drawable stickerDrawable = getResources().getDrawable(R.drawable.santa_sticker_1);
//            stickerView.addSticker(stickerDrawable);
            binding.stickerView.addSticker(stickerDrawable);
        });
    }



    private boolean isImageFromGallery(Intent intent) {
        // Check if the intent has extra information indicating that the image is from the gallery
        return intent.hasExtra("fromGallery") && intent.getBooleanExtra("fromGallery", false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                // Image captured from the camera
                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                imgReceivedFromUploadPhoto.setImageBitmap(photo);
                binding.imgReceived.setImageBitmap(photo);
//                saveImageToGallery(photo);
            } else if (requestCode == PICK_REQUEST) {
                Uri selectedImageUri = data.getData();
//                imgReceivedFromUploadPhoto.setImageURI(selectedImageUri);
                binding.imgReceived.setImageURI(selectedImageUri);
//                saveImageToGallery(selectedImageUri);
            }
        }
    }


    private void saveImageToGallery(Bitmap bitmap) {
        String savedImageURL = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                "Image",
                "Image saved from Santa App"
        );
        if (savedImageURL != null) {
        } else {
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }

//    Nov 27, 2023  -   This share Image function is used for the share the image for any platform.
    private void shareImage(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        BitmapDrawable drawable = (BitmapDrawable) binding.imgReceived.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Image", null);
        Uri imageUri = Uri.parse(path);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this image from Santa App!");
        startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }

//    Nov 27, 2023 - Use below function save the image into a gallery.
    private void saveImageToGallery(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            saveImageToGallery(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}