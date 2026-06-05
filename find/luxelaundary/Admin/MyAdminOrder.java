package com.find.luxelaundary.Admin;
 
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.find.luxelaundary.Adapter.MyOrderAdapter;
import com.find.luxelaundary.Model.Order;
import com.find.luxelaundary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyAdminOrder extends AppCompatActivity {


    private Button btnPending, btnCompleted, btnCanceled;

    private MyOrderAdapter adapter;
    ImageView btn_back;
    private ArrayList<Order> orderList = new ArrayList<>();

    private DatabaseReference ordersRef;
    private RecyclerView rvOrders;
    private String currentUserId = "";
    private String role = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        // Assuming you store user id in SharedPreferences
        currentUserId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("id", "");
        role = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("role", "");
        //color for status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white)); // Use your color here
        }

        btn_back = findViewById(R.id.btn_back);
        rvOrders = findViewById(R.id.rvOrders);
        btnPending = findViewById(R.id.btnPending);
        btnCompleted = findViewById(R.id.btnCompleted);
        btnCanceled = findViewById(R.id.btnCanceled);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new MyOrderAdapter();
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        rvOrders.setAdapter(adapter);

        ordersRef = FirebaseDatabase.getInstance().getReference("All_Order");

        loadOrders();

        btnPending.setOnClickListener(v -> filterOrders("Pending"));
        btnCompleted.setOnClickListener(v -> filterOrders("Completed"));
        btnCanceled.setOnClickListener(v -> filterOrders("Canceled"));
    }

    private void loadOrders() {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot orderSnap : snapshot.getChildren()) {
                    Order order = orderSnap.getValue(Order.class);
                    if(role.matches("0"))
                    {
                        if (order != null && order.getUserId() != null && order.getUserId().equals(currentUserId)) {
                            orderList.add(order);
                        }
                    }
                    else{
                        if (order != null && order.getUserId() != null) {
                            orderList.add(order);
                        }
                    }

                }
                adapter.setOrders(orderList,"1");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyAdminOrder.this, "Failed to load orders.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterOrders(String status) {
        ArrayList<Order> filtered = new ArrayList<>();
        for (Order order : orderList) {
            if (order.getStatus() != null && order.getStatus().equalsIgnoreCase(status)) {
                filtered.add(order);
            }
        }
        adapter.setOrders(filtered,"1");
    }
}