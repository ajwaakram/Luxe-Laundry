package com.find.luxelaundary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class StainDetectionActivity extends AppCompatActivity {
    ImageView back, cam;
    Button detect;
    Uri selectedImageUri;
    Bitmap selectedBitmap;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stain_detection);

        back = findViewById(R.id.back);
        cam = findViewById(R.id.cam);
        detect = findViewById(R.id.detect);

        back.setOnClickListener(v -> finish());

        cam.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
        });

        detect.setOnClickListener(v -> {
            if (selectedBitmap != null) {
                String severity = detectStainLevel(selectedBitmap);
                Intent intent = new Intent(StainDetectionActivity.this, StainDescription.class);
                intent.putExtra("imageUri", selectedImageUri.toString());
                intent.putExtra("severity", severity);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                selectedBitmap = BitmapFactory.decodeStream(inputStream);
                cam.setImageBitmap(selectedBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String detectStainLevel(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int darkPixels = 0;
        int totalPixels = width * height;

        for (int x = 0; x < width; x += 10) {
            for (int y = 0; y < height; y += 10) {
                int pixel = bitmap.getPixel(x, y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                int brightness = (red + green + blue) / 3;
                if (brightness < 100) darkPixels++;
            }
        }

        float darkRatio = (float) darkPixels / (totalPixels / 100);
        if (darkRatio < 5) return "None";
        else if (darkRatio < 15) return "Medium";
        else return "Severe";
    }
}
