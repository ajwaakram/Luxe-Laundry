package com.find.luxelaundary;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Checkout_user extends AppCompatActivity {
    ImageView back;
    EditText num;
    EditText address;
    Button cnfrm;
    TextView selecteddate;
    String totalBill;
    String userId = ""; // TODO: replace with actual logged-in user ID or get from intent/session


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout_user);

        back = findViewById(R.id.back);
        num = findViewById(R.id.num);
        address = findViewById(R.id.address);
        cnfrm = findViewById(R.id.cnfrm);
        selecteddate = findViewById(R.id.date);


        Intent i = getIntent();
        totalBill = i.getStringExtra("Total");

        // Initialize dateTextView with current date
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        selecteddate.setText(sdf.format(calendar.getTime()));

        // Back button click
        back.setOnClickListener(v -> finish());

        // Date selection dialog on TextView click
        selecteddate.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    Checkout_user.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                        selecteddate.setText(sdf.format(calendar.getTime()));
                    },
                    year, month, day
            );

            // Optional: Limit date picker to next 7 days only
            Calendar maxDate = Calendar.getInstance();
            maxDate.add(Calendar.DAY_OF_MONTH, 7);
            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            datePickerDialog.show();
        });

        cnfrm.setOnClickListener(v -> {
            String phone = num.getText().toString().trim();
            String addr = address.getText().toString().trim();
            String selectedDateStr = selecteddate.getText().toString();

            if (phone.isEmpty()) {
                num.setError("Phone number is required");
                num.requestFocus();
                return;
            }
            if (addr.isEmpty()) {
                address.setError("Address is required");
                address.requestFocus();
                return;
            }

            userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("id", "");


            DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("All_Cart");
            DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("All_Order");

            // 1. Get cart items of this user
            cartRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        Toast.makeText(Checkout_user.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Map<String, Object> cartItemsMap = new HashMap<>();
                    for (DataSnapshot cartSnap : snapshot.getChildren()) {
                        cartItemsMap.put(cartSnap.getKey(), cartSnap.getValue());
                    }

                    // 2. Create order data
                    String orderId = orderRef.push().getKey();

                    Map<String, Object> orderData = new HashMap<>();
                    orderData.put("orderId", orderId);
                    orderData.put("userId", userId);
                    orderData.put("date", selectedDateStr);
                    orderData.put("phoneNumber", phone);
                    orderData.put("address", addr);
                    orderData.put("totalBill", totalBill);
                    orderData.put("status", "Pending");
                    orderData.put("cartItems", cartItemsMap);

                    // 3. Save order in All_Order
                    orderRef.child(orderId).setValue(orderData).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // 4. Delete all cart items for this user
                            for (DataSnapshot cartSnap : snapshot.getChildren()) {
                                cartRef.child(cartSnap.getKey()).removeValue()
                                        .addOnFailureListener(e ->
                                                Toast.makeText(Checkout_user.this, "Failed to delete cart item: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                            }

                            Toast.makeText(Checkout_user.this, "Order placed successfully", Toast.LENGTH_SHORT).show();

                            // Go to confirm activity
                            Intent intent = new Intent(Checkout_user.this, confirm.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Checkout_user.this, "Failed to place order", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(Checkout_user.this, "Failed to get cart items: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
