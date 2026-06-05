package com.find.luxelaundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.find.luxelaundary.Admin.homescreen_admin;
import com.find.luxelaundary.User.Homescreen_user;
import com.find.luxelaundary.helper.SharedPrefManager;

public class splash extends AppCompatActivity {
    Button Username;
    TextView admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // ✅ Check if user is logged in
        if (SharedPrefManager.isLoggedIn(this)) {
            if (SharedPrefManager.isAdmin(this)) {
                // Admin user
                Intent intent = new Intent(splash.this, homescreen_admin.class);
                startActivity(intent);
                finish();
                return;
            } else if (SharedPrefManager.isUser(this)) {
                // Normal user
                Intent intent = new Intent(splash.this, Homescreen_user.class);
                startActivity(intent);
                finish();
                return;
            }
        }

        // 🔹 If not logged in, continue with splash screen
        Username = findViewById(R.id.Username);
        admin = findViewById(R.id.admin);

        Username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent user = new Intent(splash.this, Register_user.class);
                startActivity(user);
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent admin = new Intent(splash.this, Login_user.class);
                startActivity(admin);
            }
        });
    }
}
