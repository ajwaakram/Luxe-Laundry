package com.find.luxelaundary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class Forgot_Password_user extends AppCompatActivity {
EditText password;
EditText password_retype;
ImageView eye;
ImageView view;
Button ResetButton;
TextView backtologin;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password_user);

        password=findViewById(R.id.password);
        password_retype=findViewById(R.id.password_retype);
        eye=findViewById(R.id.eye);
        view=findViewById(R.id.view);
        ResetButton=findViewById(R.id.ResetButton);
        backtologin=findViewById(R.id.backtologin);

        ResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass1 = password.getText().toString().trim();
                String pass2 = password_retype.getText().toString().trim();

                if (TextUtils.isEmpty(pass1) || TextUtils.isEmpty(pass2)) {
                    Toast.makeText(Forgot_Password_user.this, "Please enter both fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!pass1.equals(pass2)) {
                    Toast.makeText(Forgot_Password_user.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get userId from SharedPreferences

                String userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("id", "");


                // Update password in Firebase Authentication
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    user.updatePassword(pass1)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Update in Realtime Database
                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("All_Users").child(userId);
                                    userRef.child("password").setValue(pass1)
                                            .addOnCompleteListener(dbTask -> {
                                                if (dbTask.isSuccessful()) {
                                                    showSuccessDialog(); // Show custom dialog
                                                } else {
                                                    Toast.makeText(Forgot_Password_user.this, "Failed to update database", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Toast.makeText(Forgot_Password_user.this, "Failed to update Auth password", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(Forgot_Password_user.this, "No logged-in user", Toast.LENGTH_SHORT).show();
                }
            }
        });
        backtologin .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count +=1;
                if(count %2==0)
                {
                    eye.setImageResource(R.drawable.visible);
                    password.setTransformationMethod(new PasswordTransformationMethod());
                }
                else{
                    eye.setImageResource(R.drawable.visibility);
                    password.setTransformationMethod(null);
                }

            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count +=1;
                if(count %2==0)
                {
                    view.setImageResource(R.drawable.visible);
                    password_retype.setTransformationMethod(new PasswordTransformationMethod());
                }
                else{
                    view.setImageResource(R.drawable.visibility);
                    password_retype.setTransformationMethod(null);
                }

            }
        });

    }
    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Forgot_Password_user.this);
        builder.setTitle("Success")
                .setMessage("Your password has been reset successfully.")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    startActivity(new Intent(Forgot_Password_user.this, Login_user.class));
                    finish();
                })
                .setCancelable(false)
                .show();
    }

}