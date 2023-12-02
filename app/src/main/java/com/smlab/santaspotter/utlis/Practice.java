package com.smlab.santaspotter.utlis;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.smlab.santaspotter.R;
import com.smlab.santaspotter.UploadPhoto;

import java.io.File;
import java.io.IOException;

public class Practice extends AppCompatActivity {

    String currentPhotoPath;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName = "photo";
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    File imageFile = File.createTempFile(fileName, ".jpg", storageDir);
                    currentPhotoPath = imageFile.getAbsolutePath();
                    uri = FileProvider.getUriForFile(Practice.this, "com.smlab.santaspotter.fileprovider", imageFile);
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(cameraIntent, 100);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==100 && resultCode==RESULT_OK){
            Bitmap img = BitmapFactory.decodeFile(currentPhotoPath);
            ImageView capture;
            capture = findViewById(R.id.imageCapture);
            capture.setImageBitmap(img);
        }
    }
}