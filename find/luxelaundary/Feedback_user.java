package com.find.luxelaundary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.find.luxelaundary.Model.FeedbackModel;
import com.find.luxelaundary.User.Homescreen_user;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Feedback_user extends AppCompatActivity {
    ImageView back, home;
    Button publish;
    ImageView star1, star2, star3, star4, star5;
    EditText text;

    int rating = 0;  // Store the current rating from 0 to 5

    DatabaseReference databaseFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_user);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white)); // Use your color here
        }

        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        text = findViewById(R.id.text);
        publish = findViewById(R.id.publish);

        databaseFeedback = FirebaseDatabase.getInstance().getReference("All_Feedback");

        back.setOnClickListener(v -> {
            startActivity(new Intent(Feedback_user.this, Homescreen_user.class));
            finish();
        });

        home.setOnClickListener(v -> {
            startActivity(new Intent(Feedback_user.this, Homescreen_user.class));
            finish();
        });

        // Star click listeners - update rating and UI
        star1.setOnClickListener(v -> setRating(1));
        star2.setOnClickListener(v -> setRating(2));
        star3.setOnClickListener(v -> setRating(3));
        star4.setOnClickListener(v -> setRating(4));
        star5.setOnClickListener(v -> setRating(5));

        publish.setOnClickListener(v -> {
            String review = text.getText().toString().trim();

            if (rating == 0) {
                Toast.makeText(Feedback_user.this, "Please select a rating", Toast.LENGTH_SHORT).show();
                return;
            }
            if (review.isEmpty()) {
                text.setError("Review is required");
                text.requestFocus();
                return;
            }

            // Retrieve userId from SharedPreferences

            String userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("name", "");


            // Create a unique feedback id
            String feedbackId = databaseFeedback.push().getKey();

            FeedbackModel feedback = new FeedbackModel( String.valueOf(rating), review,userId);

            databaseFeedback.child(feedbackId).setValue(feedback)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(Feedback_user.this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
                        // Optionally clear input
                        setRating(0);
                        text.setText("");
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(Feedback_user.this, "Failed to submit feedback. Try again.", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    // Update star images based on rating
    private void setRating(int newRating) {
        rating = newRating;

        star1.setImageResource(rating >= 1 ? R.drawable.star__1_ : R.drawable.star);
        star2.setImageResource(rating >= 2 ? R.drawable.star__1_ : R.drawable.star);
        star3.setImageResource(rating >= 3 ? R.drawable.star__1_ : R.drawable.star);
        star4.setImageResource(rating >= 4 ? R.drawable.star__1_ : R.drawable.star);
        star5.setImageResource(rating >= 5 ? R.drawable.star__1_ : R.drawable.star);
    }

    // Feedback model class

}
