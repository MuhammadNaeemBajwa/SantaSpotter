package com.smlab.santaspotter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class AddSantaActivity extends AppCompatActivity implements EraserFragment.Listener {
    private static final String TAG = "AddSantaActivity";
    RelativeLayout parentImageRelativeLayout, deleteRelativeLayout;
    ImageView imgReceivedFromUploadPhoto, share, photoEditImageView, resultBitmap;
    TextView backgroundTitle, textView_santa;
    FrameLayout container;
    Button btnCaptureImage, btnGalleryImage, btnSantaCap;
    StickerView stickerView;
    ConstraintLayout photo_editor_sdk_layout;

    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;


    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_santa);

        Objects.requireNonNull(getSupportActionBar()).hide();

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
        textView_santa = findViewById(R.id.textView_santa);
        stickerView = findViewById(R.id.stickerView);
        container = findViewById(R.id.container);
        resultBitmap = findViewById(R.id.resultBitmap);
    }

    private Bitmap getBitmap() {
        return drawableToBitmap(ResourcesCompat.getDrawable(getResources(), R.drawable.santa_sticker_1, null));
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
        } else {

        }

        // 04/09/2023 Use the receivedBitmap as needed in your CompanyName activity
        Bitmap receivedBitmap = getIntent().getParcelableExtra("imageBitmap");
        if (receivedBitmap != null) {
            imgReceivedFromUploadPhoto.setImageBitmap(receivedBitmap);
        } else {

        }

        btnSantaCap.setOnClickListener(view -> startActivity(new Intent(AddSantaActivity.this, SelectSanta.class)));
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

        share.setOnClickListener(view -> {
            shareImage();
        });

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new EraserFragment(this))
                .commit();

        backgroundTitle.setOnClickListener(view -> {
            container.setVisibility(View.VISIBLE);
            resultBitmap.setVisibility(View.GONE);
//            photo_editor_sdk_layout.setVisibility(View.VISIBLE);
//            photoEditorSDK.setBrushDrawingMode(true);
        });
        textView_santa.setOnClickListener(view -> {
            container.setVisibility(View.GONE);
            resultBitmap.setVisibility(View.GONE);
//            photoEditorSDK.brushEraser();
        });
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
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
        if (requestCode == 4455) {
            byte[] receivedBytes = data.getByteArrayExtra("bitmapBytesExtra");
            stickerView.addSticker(getBitmapFromBytes(receivedBytes));
        }
    }

    public static Bitmap getBitmapFromBytes(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    @Override
    public void onSaveBitmap(Bitmap bitmap) {
        Log.d(TAG, "onSaveBitmap: ");
        container.setVisibility(View.GONE);
        resultBitmap.setVisibility(View.VISIBLE);
        resultBitmap.setImageBitmap(bitmap);
    }
}