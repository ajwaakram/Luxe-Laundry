package com.find.luxelaundary.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.find.luxelaundary.Model.SubService;
import com.find.luxelaundary.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addSubServices extends AppCompatActivity {
    EditText subServiceName, subServicePrice, subServiceDescription;
    Button saveSubService;
    TextView serviceTitle,viewSubService;
    String serviceID, serviceName;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_sub_services);
        EdgeToEdge.enable(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }


        viewSubService = findViewById(R.id.viewSubService);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        subServiceName = findViewById(R.id.subServiceName);
        subServicePrice = findViewById(R.id.subServicePrice);
        subServiceDescription = findViewById(R.id.subServiceDescription);
        saveSubService = findViewById(R.id.saveSubService);
        serviceTitle = findViewById(R.id.serviceTitle);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Saving...");

        // Get intent values
        serviceID = getIntent().getStringExtra("id");
        serviceName = getIntent().getStringExtra("name");
        serviceTitle.setText("Add Sub-Service " + serviceName);
        viewSubService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addSubServices.this, viewSubServices.class);
                intent.putExtra("id", serviceID);
                intent.putExtra("name", serviceName);
                startActivity(intent);
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("All_Sub_Services");

        saveSubService.setOnClickListener(v -> {
            String name = subServiceName.getText().toString().trim();
            String price = subServicePrice.getText().toString().trim();
            String description = subServiceDescription.getText().toString().trim();

            // Validation
            if (TextUtils.isEmpty(name)) {
                subServiceName.setError("Enter name");
                return;
            }
            if (TextUtils.isEmpty(price)) {
                subServicePrice.setError("Enter price");
                return;
            }
            if (TextUtils.isEmpty(description)) {
                subServiceDescription.setError("Enter description");
                return;
            }

            progressDialog.show();

            // Generate ID
            String id = databaseReference.push().getKey();

            SubService subService = new SubService(id, serviceID, name, price, description);
            databaseReference.child(id).setValue(subService)
                    .addOnCompleteListener(task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(addSubServices.this, "Sub-Service added", Toast.LENGTH_SHORT).show();
                            subServiceName.setText("");
                            subServicePrice.setText("");
                            subServiceDescription.setText("");
                        } else {
                            Toast.makeText(addSubServices.this, "Error saving data", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}