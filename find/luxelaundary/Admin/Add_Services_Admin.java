package com.find.luxelaundary.Admin;


import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.find.luxelaundary.Model.Service;
import com.find.luxelaundary.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Add_Services_Admin extends AppCompatActivity {

    EditText editServiceName, editImageUrl;
    Button btnAddService;
    ImageView back;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services_admin);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }


        back = findViewById(R.id.back);
        editServiceName = findViewById(R.id.editServiceName);
        editImageUrl = findViewById(R.id.editImageUrl);
        btnAddService = findViewById(R.id.btnAddService);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Adding Service...");
        dialog.setCancelable(false);

        btnAddService.setOnClickListener(v -> {
            String name = editServiceName.getText().toString().trim();
            String imgUrl = editImageUrl.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                editServiceName.setError("Service name required");
                return;
            }

            if (TextUtils.isEmpty(imgUrl)) {
                editImageUrl.setError("Image URL required");
                return;
            }

            addServiceToFirebase(name, imgUrl);
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addServiceToFirebase(String name, String imgUrl) {
        dialog.show();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("All_Services");
        String id = dbRef.push().getKey();

        Service service = new Service(id, name, imgUrl);

        dbRef.child(id).setValue(service).addOnCompleteListener(task -> {
            dialog.dismiss();
            if (task.isSuccessful()) {
                Toast.makeText(this, "Service added successfully", Toast.LENGTH_SHORT).show();
                editServiceName.setText("");
                editImageUrl.setText("");
            } else {
                Toast.makeText(this, "Failed to add service", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
