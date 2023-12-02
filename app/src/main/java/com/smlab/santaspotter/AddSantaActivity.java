package com.smlab.santaspotter;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.smlab.santaspotter.baseclasses.BaseActivity;
import com.smlab.santaspotter.databinding.ActivityAddSantaBinding;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class AddSantaActivity extends BaseActivity {

    //    private static final String TAG = AddSantaActivity.class.getSimpleName();
    private static final String TAG = "AddSantaActivity:";
    private static final int SELECT_SANTA_REQUEST = 54;
    StickerView stickerView;
    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;
    Uri uri;
    private ActivityAddSantaBinding binding;
    Dialog dialogCode;

    String encodedImageData = "";

    private static final int CAMERA_PERMISSION_REQUEST = 123;

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

    String captureImagePath;

    private void imageSet() {
        Intent intent = getIntent();
        uri = intent.getParcelableExtra("img");
//        Dec 02, 2023  -   (U) The image change from bitmap to file
        captureImagePath = intent.getStringExtra("capturedImage");
        if (captureImagePath != null) {
            setImageOrientationWithProperOrientation(captureImagePath);
        }

        if (uri != null) {
//
//                Nov 02, 2023  -   The image does not fit so using picasso fit function
//            binding.imgReceived.setImageURI(uri);
            Picasso.get()
                    .load(uri)
                    .rotate(getImageOrientation(uri))
                    .fit()
                    .into(binding.imgReceived);
        }

//        Dec 02 2023 - Foe now commmited for a test
        Bitmap receivedBitmap = getIntent().getParcelableExtra("imageBitmap");
        Log.d(TAG, "imageSet: receivedBitmap: 69: " + receivedBitmap);
        if (receivedBitmap != null) {
//            binding.imgReceived.setImageBitmap(receivedBitmap);

            File file = new File(getCacheDir(), "image.png");
            try {
                FileOutputStream fos = new FileOutputStream(file);
                receivedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

//
//                Nov 02, 2023  -   The image does not fit so using picasso fit function
            Picasso.get()
//                    .load(receivedBitmap)
                    .load(file)
                    .fit() // or .centerCrop() depending on your requirement
                    .into(binding.imgReceived);
        }
    }

    private Uri getImageUri(Context context, File file) {
        return FileProvider.getUriForFile(context, "com.smlab.santaspotter.FileProvider", file);
    }

    private int getImageOrientation(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            ExifInterface exif = new ExifInterface(inputStream);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }


    // Method to rotate the bitmap based on the Exif orientation
    private Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;
            default:
                return bitmap; // No rotation needed
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    //  Nov 29, 2023  -   This method will show your dialog.
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
//            launchCamera();
        });

        binding.constraintUploadGallery.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_REQUEST);
        });
        binding.constraintShare.setOnClickListener(view -> {
            shareImage();
        });


    }

    private boolean isImageFromGallery(Intent intent) {
        return intent.hasExtra("fromGallery") && intent.getBooleanExtra("fromGallery", false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST || requestCode == SELECT_SANTA_REQUEST) {
                previousCode(requestCode, resultCode, data);
//                if (captureImagePath != null) {
//                    setImageOrientationWithProperOrientation(captureImagePath);
//                }

            } else if (requestCode == PICK_REQUEST) {
                // Handle result from gallery
                Uri selectedImageUri = data.getData();

                // Set the selected image from the gallery as the background
                Log.d(TAG, "onActivityResult: setImageUri: 228: ");
//                Nov 02, 2023  -   The image does not fit so using picasso fit function
//                binding.imgReceived.setImageURI(selectedImageUri);
                Picasso.get()
                        .load(selectedImageUri)
                        .rotate(getImageOrientation(selectedImageUri))
                        .fit()
                        .into(binding.imgReceived);

                int selectedStickerResId = data.getIntExtra("selectedSticker", -1);
                if (selectedStickerResId != -1) {
                    // Add the selected sticker to the StickerView
                    binding.stickerView.addSticker(BitmapFactory.decodeResource(getResources(), selectedStickerResId));
                }
            }
        }
    }


    // Save the bitmap to a file and return the file path
    private String saveBitmapToFile(Bitmap bitmap) {
        String filename = "combined_image_" + System.currentTimeMillis() + ".jpg";

        // Create a directory to store images
        File directory = new File(getFilesDir(), "CombinedImages");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Create the file
        File file = new File(directory, filename);

        try {
            // Compress and save the bitmap to the file
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, e.g., show an error message
            return null;
        }
    }

    // Save the bitmap to a file and return the file path
    private String saveBitmapToFileUpdate(Bitmap bitmap) {
        String filename = "combined_image_" + System.currentTimeMillis() + ".jpg";

        // Create a directory to store images
        File directory = new File(getFilesDir(), "CombinedImages");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Create the file
        File file = new File(directory, filename);

        try {
            // Compress and save the bitmap to the file
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, e.g., show an error message
            return null;
        }
    }

    // Insert the image into the Media Provider (Gallery)
    private void insertImageIntoGallery(String imagePath) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "SantaImage");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image with Santa sticker");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATA, imagePath);

        ContentResolver resolver = getContentResolver();
        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    // Save the combined bitmap to the gallery
    private void saveImageToGalleryUpdate(Bitmap bitmap) {
        String imagePath = saveBitmapToFileUpdate(bitmap);
        if (imagePath != null) {
            insertImageIntoGallery(imagePath);
            Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }

    // Call this method when you want to save the image
    private void saveImageWithStickers() {
        // Get the combined image from the ImageView
        Bitmap combinedBitmap = ((BitmapDrawable) binding.imgReceived.getDrawable()).getBitmap();
        // Save the image to the gallery
        saveImageToGalleryUpdate(combinedBitmap);
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
        Log.d(TAG, "shareImage: imgReceived: 361: ");
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

    private void setImageOrientationWithProperOrientation(String imagePath) {
//        captureImagePath = intent.getStringExtra("capturedImage");
        if (imagePath != null) {
            Bitmap img = BitmapFactory.decodeFile(imagePath);

            // Check and handle image orientation
            try {
                ExifInterface exifInterface = new ExifInterface(imagePath);
                int orientation = exifInterface.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED
                );

                img = rotateBitmap(img, orientation);
            } catch (IOException e) {
                e.printStackTrace();
            }

            File file = new File(getCacheDir(), "image.png");
            try {
                FileOutputStream fos = new FileOutputStream(file);
                img.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Picasso.get()
                    .load(file)
                    .fit()
                    .into(binding.imgReceived);
        }
    }

    void previousCode(int requestCode, int resultCode, @Nullable Intent data) {

        Bitmap photo = (Bitmap) data.getExtras().get("data");
        // Get the existing image from the binding
        Log.d(TAG, "onActivityResult: getDrawable: 187: ");
        Bitmap existingImage = ((BitmapDrawable) binding.imgReceived.getDrawable()).getBitmap();

        int selectedStickerResId = data.getIntExtra("selectedSticker", -1);

        if (selectedStickerResId != -1) {
            Bitmap combinedBitmap = Bitmap.createBitmap(existingImage.getWidth(), existingImage.getHeight(), existingImage.getConfig());
            Canvas canvas = new Canvas(combinedBitmap);
            canvas.drawBitmap(existingImage, new Matrix(), null);
            // Add the selected sticker to the StickerView
            binding.stickerView.addSticker(BitmapFactory.decodeResource(getResources(), selectedStickerResId));

//                  Nov 29, 2023    -   On touch of sticker then dialog permission dialog show .
//                  After dismiss the dialog, 0.5sec delay  the moved towards the edit activity

//                    binding.stickerView.setStickerTouchListener(() -> {
//                        showCustomDialog();

            Intent nextActivityIntent = new Intent(AddSantaActivity.this, EditSantaActivity.class);
//                    Intent nextActivityIntent = new Intent(AddSantaActivity.this, AddSantaActivity.class);
            String combinedImagePath = saveBitmapToFile(combinedBitmap);
            nextActivityIntent.putExtra("combinedImagePath", combinedImagePath);
            nextActivityIntent.putExtra("selectedStickerDrawable", selectedStickerResId);

            startActivity(nextActivityIntent);

//                    });
//                            startActivity(nextActivityIntent);
            finish();
//                        }, 500);
//                    });

        } else {
            // Handle result from the camera without a sticker
            Log.d(TAG, "onActivityResult: setImageBitmap: 220: ");
//                    binding.imgReceived.setImageBitmap(photo);

            if (photo != null) {
//            binding.imgReceived.setImageBitmap(receivedBitmap);

                File file = new File(getCacheDir(), "image.png");
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    photo.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//
//                Nov 02, 2023  -   The image does not fit so using picasso fit function
                Picasso.get()
//                    .load(receivedBitmap)
                        .load(file)
                        .fit() // or .centerCrop() depending on your requirement
                        .into(binding.imgReceived);
            }


        }
    }
    String currentPhotoPath;
    private void launchCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        } else {
            // Permission already granted, launch camera intent


//  Dec 02, 2023    -   (U) Make a path for save the captured Image
            String fileName = "photo";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File imageFile = File.createTempFile(fileName, ".jpg", storageDir);
                currentPhotoPath = imageFile.getAbsolutePath();
                uri = FileProvider.getUriForFile(AddSantaActivity.this, "com.smlab.santaspotter.FileProvider", imageFile);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }


}