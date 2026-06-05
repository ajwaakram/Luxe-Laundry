package com.find.luxelaundary.Admin;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.find.luxelaundary.Adapter.AdminUserAdapter;
import com.find.luxelaundary.Adapter.Admin_FeedbackAdapter;
import com.find.luxelaundary.Model.FeedbackModel;
import com.find.luxelaundary.Model.User;
import com.find.luxelaundary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewFeedback extends AppCompatActivity {

    RecyclerView allUsserRecy;
    ImageView back;

//3 step
    List<FeedbackModel> serviceList;
    Admin_FeedbackAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_feedback);
//color for status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white)); // Use your color here
        }

        back = findViewById(R.id.back);
        allUsserRecy = findViewById(R.id.allUsserRecy);

        back.setOnClickListener(v -> finish());

        serviceList = new ArrayList<>();
        adapter = new Admin_FeedbackAdapter(this, serviceList);
//layout recyclermanager 2 step
        allUsserRecy.setLayoutManager(new LinearLayoutManager(this));
        allUsserRecy.setAdapter(adapter);

        loadServices();
    }
    private void loadServices() {
      //database s data fetch code
        FirebaseDatabase.getInstance().getReference("All_Feedback")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        serviceList.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            FeedbackModel user = snap.getValue(FeedbackModel.class);
                            if (user != null) {
                                serviceList.add(user);
                            }
                        }
                        adapter.notifyDataSetChanged(); // ✅ just notify, don’t recreate adapter
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ViewFeedback.this, "Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}