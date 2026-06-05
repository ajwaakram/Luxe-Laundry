package com.find.luxelaundary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.find.luxelaundary.Admin.homescreen_admin;
import com.find.luxelaundary.User.Homescreen_user;
import com.find.luxelaundary.helper.SharedPrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login_user extends AppCompatActivity {

    EditText usernameInput, passwordInput;
    ImageView visiblePass;
    TextView forgotPassword, createAccount;
    Button loginButton;
    int count = 0;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_user);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        visiblePass = findViewById(R.id.visiblePass);
        forgotPassword = findViewById(R.id.forgotPassword);
        loginButton = findViewById(R.id.loginButton);
        createAccount = findViewById(R.id.createAccount);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Logging in...");
        dialog.setCancelable(false);

        loginButton.setOnClickListener(v -> {
            String email = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty()) {
                usernameInput.setError("Email is required");
                return;
            }

            if (password.isEmpty()) {
                passwordInput.setError("Password is required");
                return;
            }

            loginUser(email, password);
        });

        forgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(Login_user.this, Forgot_Password_user.class));
        });

        createAccount.setOnClickListener(v -> {
            startActivity(new Intent(Login_user.this, Register_user.class));
        });

        visiblePass.setOnClickListener(v -> {
            count++;
            if (count % 2 == 0) {
                visiblePass.setImageResource(R.drawable.visible);
                passwordInput.setTransformationMethod(new PasswordTransformationMethod());
            } else {
                visiblePass.setImageResource(R.drawable.visibility);
                passwordInput.setTransformationMethod(null);
            }
        });
    }

    private void loginUser(String email, String password) {
        dialog.show();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("All_Users");

        dbRef.get().addOnCompleteListener(task -> {
            dialog.dismiss();
            if (task.isSuccessful()) {
                boolean found = false;
                for (DataSnapshot userSnapshot : task.getResult().getChildren()) {
                    String dbEmail = userSnapshot.child("email").getValue(String.class);
                    String dbPassword = userSnapshot.child("password").getValue(String.class);

                    if (dbEmail != null && dbPassword != null && dbEmail.equals(email) && dbPassword.equals(password)) {
                        // Match found
                        String id = userSnapshot.child("id").getValue(String.class);
                        String name = userSnapshot.child("name").getValue(String.class);
                        String role = userSnapshot.child("role").getValue(String.class);

                        SharedPrefManager.saveUser(Login_user.this, id, name, dbEmail, role);

                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                        if ("1".equals(role)) {
                            startActivity(new Intent(Login_user.this, homescreen_admin.class));
                        } else {
                            startActivity(new Intent(Login_user.this, Homescreen_user.class));
                        }
                        finish();
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Database error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
