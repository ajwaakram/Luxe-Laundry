package com.find.luxelaundary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.Random;

public class StainDescription extends AppCompatActivity {

    ImageView back, pic;
    TextView type, severity, fabric, recommendation;
    Button again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stain_description);

        back = findViewById(R.id.back);
        pic = findViewById(R.id.pic);
        type = findViewById(R.id.stain_type);
        severity = findViewById(R.id.severity_level);
        fabric = findViewById(R.id.fabric_type);
        recommendation = findViewById(R.id.recommendation);
        again = findViewById(R.id.btn_again);

        back.setOnClickListener(v -> finish());

        again.setOnClickListener(v -> {
            Intent intent = new Intent(this, StainDetectionActivity.class);
            startActivity(intent);
            finish();
        });

        String severityLevel = getIntent().getStringExtra("severity");
        String imageUri = getIntent().getStringExtra("imageUri");

        if (imageUri != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(Uri.parse(imageUri));
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                pic.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (severityLevel == null || severityLevel.equalsIgnoreCase("None")) {
            type.setText("No stain detected");
            severity.setText("Normal");
            fabric.setText("N/A");
            recommendation.setText("");
            recommendation.setVisibility(TextView.GONE);
        } else {
            String[] stains = {"Tea", "Ink", "Oil", "Blood", "Grease", "Ice Cream", "Paint", "Coffee", "Chocolate", "Sauce"};
            String stainType = getRandomStainType(stains);
            String fabricType = "Cotton"; // or randomize later

            type.setText(stainType);
            severity.setText(severityLevel);
            fabric.setText(fabricType);
            recommendation.setText(getRecommendation(stainType, severityLevel, fabricType));
            recommendation.setVisibility(TextView.VISIBLE);
        }
    }

    private String getRandomStainType(String[] stainTypes) {
        Random random = new Random();
        return stainTypes[random.nextInt(stainTypes.length)];
    }

    private String getRecommendation(String stain, String severity, String fabric) {
        if (stain.equalsIgnoreCase("Ink")) {
            return "Soak in a mixture of water and lemon juice for 15 minutes, then rinse gently.";
        } else if (stain.equalsIgnoreCase("Tea")) {
            return "Use Surf Excel, soak 15 mins, scrub gently and rinse.";
        } else if (stain.equalsIgnoreCase("Oil")) {
            return "Sprinkle baking soda, wait 10 mins, brush off, and wash.";
        } else if (stain.equalsIgnoreCase("Blood")) {
            return "Use cold water, apply hydrogen peroxide, then rinse.";
        } else if (stain.equalsIgnoreCase("Grease")) {
            return "Apply dish soap directly, scrub gently, and rinse.";
        } else if (stain.equalsIgnoreCase("Ice Cream")) {
            return "Soak in cold water and use a stain remover.";
        } else if (stain.equalsIgnoreCase("Paint")) {
            return "Use paint thinner (if fabric allows), then rinse.";
        } else if (stain.equalsIgnoreCase("Coffee")) {
            return "Blot with vinegar solution and rinse in cold water.";
        } else if (stain.equalsIgnoreCase("Chocolate")) {
            return "Scrape off chocolate, soak in detergent and warm water.";
        } else if (stain.equalsIgnoreCase("Sauce")) {
            return "Rinse with cold water, apply detergent, and wash.";
        }

        return "Apply detergent and follow standard washing instructions.";
    }
}
