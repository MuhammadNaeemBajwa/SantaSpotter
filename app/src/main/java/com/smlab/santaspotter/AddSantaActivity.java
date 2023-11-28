package com.smlab.santaspotter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

public class AddSantaActivity extends AppCompatActivity {
    private static final int SELECT_SANTA_REQUEST = 54;
    StickerView stickerView;
    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;
    Uri uri;
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
        Intent intent = getIntent();
        uri = intent.getParcelableExtra("img");
        binding.imgReceived.setImageURI(uri);
        if (isImageFromGallery(intent)) {
        }
//       04/09/2023 Use the receivedBitmap as needed in your CompanyName activity
        Bitmap receivedBitmap = getIntent().getParcelableExtra("imageBitmap");
        if (receivedBitmap != null) {
            binding.imgReceived.setImageBitmap(receivedBitmap);
        } else {
        }

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
//        binding.textViewBackground.setOnClickListener(view -> {
//            binding.stickerView.setVisibility(View.VISIBLE);
//            Drawable stickerDrawable = getResources().getDrawable(R.drawable.santa2);
//            binding.stickerView.addSticker(stickerDrawable);
//        });
    }

    private boolean isImageFromGallery(Intent intent) {
        return intent.hasExtra("fromGallery") && intent.getBooleanExtra("fromGallery", false);
    }

    // Nov 28, 2023 -   onActivityResult we overlay the sticker on the capture image/upload image from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST || requestCode == SELECT_SANTA_REQUEST) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                // Get the existing image from the binding
                Bitmap existingImage = ((BitmapDrawable) binding.imgReceived.getDrawable()).getBitmap();

                int selectedStickerResId = data.getIntExtra("selectedSticker", -1);

                if (selectedStickerResId != -1) {
                    // Set the selected sticker on the image in AddSantaActivity
                    Drawable selectedStickerDrawable = getResources().getDrawable(selectedStickerResId);

                    // Create a canvas to overlay the sticker on the existing image
                    Bitmap combinedBitmap = Bitmap.createBitmap(existingImage.getWidth(), existingImage.getHeight(), existingImage.getConfig());
//                    Bitmap combinedBitmap = Bitmap.createScaledBitmap(existingImage, 800, 500, true);
                    Canvas canvas = new Canvas(combinedBitmap);
                    canvas.drawBitmap(existingImage, new Matrix(), null);
//                    canvas.drawBitmap(BitmapUtils.drawableToBitmap(selectedStickerDrawable), new Matrix(), null);

                    // Set the combined image to the ImageView
                    binding.imgReceived.setImageBitmap(combinedBitmap);

                    // Add the selected sticker to the StickerView
                    binding.stickerView.addSticker(selectedStickerDrawable);

                    // Pass both the image and sticker to the next activity
                    Intent nextActivityIntent = new Intent(AddSantaActivity.this, EditSantaActivity.class);
                    nextActivityIntent.putExtra("combinedBitmap", combinedBitmap);
                    nextActivityIntent.putExtra("selectedStickerDrawable", selectedStickerResId);
                    startActivity(nextActivityIntent);


                } else {
                    // Handle result from the camera without a sticker
                    binding.imgReceived.setImageBitmap(photo);
                }
            } else if (requestCode == PICK_REQUEST) {
                // Handle result from gallery
                Uri selectedImageUri = data.getData();

                // Set the selected image from the gallery as the background
                binding.imgReceived.setImageURI(selectedImageUri);

                int selectedStickerResId = data.getIntExtra("selectedSticker", -1);

                if (selectedStickerResId != -1) {
                    // Add the selected sticker to the StickerView
                    Drawable selectedStickerDrawable = getResources().getDrawable(selectedStickerResId);
                    binding.stickerView.addSticker(selectedStickerDrawable);

                }
            }
        }
    }


//    Nov 28, 2023 -    For now below activity result is commented.
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == CAMERA_REQUEST || requestCode == SELECT_SANTA_REQUEST) {
//                // Image captured from the camera
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                binding.imgReceived.setImageBitmap(photo);
//            } else if (requestCode == PICK_REQUEST) {
//                Uri selectedImageUri = data.getData();
//                binding.imgReceived.setImageURI(selectedImageUri);
//            }
//        }
//    }

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
//      Nov 28, 2023 -  For now the share image got an error and error so I commented
    private void shareImage() {
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
//    private void saveImageToGallery(Uri imageUri) {
//        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//            saveImageToGallery(bitmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}