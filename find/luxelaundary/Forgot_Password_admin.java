package com.find.luxelaundary;

import android.content.Intent;
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

public class Forgot_Password_admin extends AppCompatActivity {
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
        setContentView(R.layout.activity_forgot_password_admin);

        password=findViewById(R.id.password);
        password_retype=findViewById(R.id.password_retype);
        eye=findViewById(R.id.eye);
        view=findViewById(R.id.view);
        ResetButton=findViewById(R.id.ResetButton);
        backtologin=findViewById(R.id.backtologin);

        ResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().isEmpty()) {
                    password.setError("Password is required");
                    Toast.makeText(Forgot_Password_admin.this, "Password is required", Toast.LENGTH_SHORT).show();
                } else if (password_retype.getText().toString().isEmpty()) {
                    password_retype.setError("Password is required");
                    Toast.makeText(Forgot_Password_admin.this, "Password is required", Toast.LENGTH_SHORT).show();
                } else {
                    {
                        Intent forget = new Intent(Forgot_Password_admin.this, Forgot_Password_admin.class);
                        Toast.makeText(Forgot_Password_admin.this, "You have successfully reset your password", Toast.LENGTH_SHORT).show();
                        startActivity(forget);
                    }

                }
            }
        });
        backtologin .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Forgot_Password_admin.this, Login_user.class);
                startActivity(intent);
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
}