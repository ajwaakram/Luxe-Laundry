package com.find.luxelaundary;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
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

import com.find.luxelaundary.Adapter.CartAdapter;
import com.find.luxelaundary.Adapter.OrderDetailAdapter;
import com.find.luxelaundary.Model.Cart;
import com.find.luxelaundary.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    TextView tvOrderId, tvDate, tvAddress, tvTotal;
    RecyclerView rvCartItems;
    OrderDetailAdapter adapter;
    ImageView btn_back;
    List<Cart> cartItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_detail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white)); // Use your color here
        }

        btn_back = findViewById(R.id.btn_back);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvDate = findViewById(R.id.tvDate);
        tvAddress = findViewById(R.id.tvAddress);
        tvTotal = findViewById(R.id.tvTotal);
        rvCartItems = findViewById(R.id.rvCartItems);

        EdgeToEdge.enable(this);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Order order = (Order) getIntent().getSerializableExtra("orderData");

        if (order != null) {
            tvOrderId.setText("Order ID: " + order.getOrderId());
            tvDate.setText("Date: " + order.getDate());
            tvAddress.setText("Address: " + order.getAddress());
            tvTotal.setText("Total "+ order.getTotalBill());
        }
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderDetailAdapter(OrderDetailActivity.this,cartItemList);
        rvCartItems.setAdapter(adapter);
        loadOrderDetails(order.getOrderId());

    }

    private void loadOrderDetails(String orderId) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("All_Order").child(orderId);

        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                cartItemList.clear();


                for (DataSnapshot itemSnap : snapshot.child("cartItems").getChildren()) {
                    Cart item = itemSnap.getValue(Cart.class);
                    if (item != null) {
                        cartItemList.add(item);

                    }
                }


                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderDetailActivity.this, "Error loading order", Toast.LENGTH_SHORT).show();
            }
        });
    }
}