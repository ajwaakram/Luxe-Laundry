package com.find.luxelaundary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.find.luxelaundary.Model.User;
import com.find.luxelaundary.helper.SharedPrefManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register_user extends AppCompatActivity {
    EditText nameInput;
    EditText passwordInput;
    EditText EmailInput;
    ImageView visiblePass;
    TextView forgotPassword;
    Button signInButton;
    TextView already;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_user);
        nameInput=findViewById(R.id.nameInput);
        passwordInput=findViewById(R.id.passwordInput);
        EmailInput=findViewById(R.id.EmailInput);
        visiblePass=findViewById(R.id.visiblePass);
        forgotPassword=findViewById(R.id.forgotPassword);
        signInButton=findViewById(R.id.signInButton);
        already=findViewById(R.id.already);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameInput.getText().toString().isEmpty()) {
                    nameInput.setError("Username is required");
                    Toast.makeText(Register_user.this, "Username is required", Toast.LENGTH_SHORT).show();
                } else if (passwordInput.getText().toString().isEmpty()) {
                    passwordInput.setError("Password is required");
                    Toast.makeText(Register_user.this, "Password is required", Toast.LENGTH_SHORT).show();
                } else if (EmailInput.getText().toString().isEmpty()) {
                    EmailInput.setError("Email is required");
                    Toast.makeText(Register_user.this, "Email is required", Toast.LENGTH_SHORT).show();
                } else {
                    {
                        saveUserData(nameInput.getText().toString(),EmailInput.getText().toString(),passwordInput.getText().toString());

                    }

                }
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register_user.this, Forgot_Password_user.class);
                startActivity(intent);
            }
        });
        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register_user.this, Login_user.class);
                startActivity(intent);
            }
        });
        visiblePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count +=1;
                if(count %2==0)
                {
                    visiblePass.setImageResource(R.drawable.visible);
                    passwordInput.setTransformationMethod(new PasswordTransformationMethod());
                }
                else{
                    visiblePass.setImageResource(R.drawable.visibility);
                    passwordInput.setTransformationMethod(null);
                }

            }
        });
    }

    public void saveUserData(String name, String email, String password) {
        ProgressDialog dialog = new ProgressDialog(Register_user.this);
        dialog.setMessage("Registering...");
        dialog.setCancelable(false);
        dialog.show();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("All_Users");

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String uid = task.getResult().getUser().getUid(); // Get Firebase Auth UID

                // Create user model
                User user = new User(uid, name, email, password,"0");

                // Save to Firebase Realtime Database
                dbRef.child(uid).setValue(user).addOnCompleteListener(dbTask -> {
                    dialog.dismiss();
                    if (dbTask.isSuccessful()) {

                        Toast.makeText(Register_user.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Register_user.this, Login_user.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Register_user.this, "Database error: " + dbTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                dialog.dismiss();
                Toast.makeText(Register_user.this, "Auth failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}