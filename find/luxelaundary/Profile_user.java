package com.find.luxelaundary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.find.luxelaundary.User.Homescreen_user;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile_user extends AppCompatActivity {

    EditText nameEditText, emailEditText;
    Button logoutBtn, updateBtn;
    ImageView back;
    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }

        nameEditText = findViewById(R.id.Username);
        emailEditText = findViewById(R.id.email);
        logoutBtn = findViewById(R.id.logoutBtn);
        updateBtn = findViewById(R.id.updateBtn);
        back = findViewById(R.id.back);

        // ✅ Updated Firebase reference to "All_Users"
        databaseReference = FirebaseDatabase.getInstance().getReference("All_Users");

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String email = sharedPreferences.getString("email", "");
        userId = sharedPreferences.getString("id", "");

        nameEditText.setText(name);
        emailEditText.setText(email);

        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateBtn.setVisibility(View.VISIBLE);
            }
            @Override public void afterTextChanged(Editable s) {}
        };

        nameEditText.addTextChangedListener(watcher);
        emailEditText.addTextChangedListener(watcher);

        logoutBtn.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(Profile_user.this, Login_user.class);
            startActivity(intent);
            finish();
        });

        back.setOnClickListener(v -> {
            Intent i = new Intent(Profile_user.this, Homescreen_user.class);
            startActivity(i);
        });

        updateBtn.setOnClickListener(v -> {
            String updatedName = nameEditText.getText().toString().trim();
            String updatedEmail = emailEditText.getText().toString().trim();

            if (updatedName.isEmpty() || updatedEmail.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Update in Firebase
            databaseReference.child(userId).child("name").setValue(updatedName);
            databaseReference.child(userId).child("email").setValue(updatedEmail);

            // ✅ Update SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", updatedName);
            editor.putString("email", updatedEmail);
            editor.apply();

            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
        });
    }
}
