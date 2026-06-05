package com.find.luxelaundary.Admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.find.luxelaundary.Adapter.AdminServiceAdapter;
import com.find.luxelaundary.Adapter.AdminSubServiceAdapter;
import com.find.luxelaundary.Model.SubService;
import com.find.luxelaundary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class viewSubServices extends AppCompatActivity {
    String serviceID,serviceName;
    TextView serviceTitle;
    ImageView back;

    RecyclerView subServiceRecycler;
    List<SubService> subServiceList;
    AdminSubServiceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_sub_services);
        serviceTitle = findViewById(R.id.serviceTitle);
        back = findViewById(R.id.back);
        subServiceRecycler = findViewById(R.id.subServiceRecycler);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        serviceID = getIntent().getStringExtra("id");
        serviceName = getIntent().getStringExtra("name");
        serviceTitle.setText("Add Sub-Service " + serviceName);
         subServiceRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        subServiceList = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("All_Sub_Services");
        dbRef.orderByChild("serviceID").equalTo(serviceID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        subServiceList.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {

                            SubService subService = snap.getValue(SubService.class);
                            subServiceList.add(subService);
                        }

                        adapter = new AdminSubServiceAdapter(viewSubServices.this, subServiceList);
                        subServiceRecycler.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(viewSubServices.this, "Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}