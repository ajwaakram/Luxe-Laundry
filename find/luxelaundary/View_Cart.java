package com.find.luxelaundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.find.luxelaundary.Adapter.CartAdapter;
import com.find.luxelaundary.Model.Cart;
import com.find.luxelaundary.User.Homescreen_user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class View_Cart extends AppCompatActivity {
    ImageView back;
    Button btn_checkout;
    RecyclerView cartRecycler;
    List<Cart> cartList;
    CartAdapter adapter;
    int total = 0;
    String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.view_cart);

        cartRecycler = findViewById(R.id.cartRecycler);
        back = findViewById(R.id.back);
        btn_checkout = findViewById(R.id.btn_checkout);

        cartRecycler.setLayoutManager(new LinearLayoutManager(this));
        cartList = new ArrayList<>();

        userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("id", "");

        back.setOnClickListener(v -> {
            Intent backIntent = new Intent(View_Cart.this, Homescreen_user.class);
            startActivity(backIntent);
        });

        btn_checkout.setOnClickListener(v -> {
            Intent checkout = new Intent(View_Cart.this, Checkout_user.class);
            checkout.putExtra("Total",String.valueOf(total));
            startActivity(checkout);
        });

        // Fetch cart items for the user from Firebase
        FirebaseDatabase.getInstance().getReference("All_Cart")
                .orderByChild("userId")
                .equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        cartList.clear();
                        total = 0;

                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Cart cart = snap.getValue(Cart.class);
                            if (cart != null) {
                                int itemTotal = Integer.parseInt(cart.getPrice()) * cart.getQuantity();
                                total += itemTotal;
                                cartList.add(cart);
                            }
                        }

                        adapter = new CartAdapter(View_Cart.this, cartList);
                        adapter.setOnItemDeletedListener(() -> calculateTotal());
                        cartRecycler.setAdapter(adapter);

                        btn_checkout.setText("PKR " + total + " Confirm");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error if needed
                    }
                });
    }

    private void calculateTotal() {
        int total = 0;
        for (Cart cart : cartList) {
            int itemTotal = Integer.parseInt(cart.getPrice()) * cart.getQuantity(); // quantity is int
            total += itemTotal;
        }
        btn_checkout.setText("PKR " + total + " Confirm");
    }
}
