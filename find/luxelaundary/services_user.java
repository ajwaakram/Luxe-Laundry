package com.find.luxelaundary;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.find.luxelaundary.Adapter.SubServiceAdapter;
import com.find.luxelaundary.Model.Cart;
import com.find.luxelaundary.Model.SubService;
import com.find.luxelaundary.User.Homescreen_user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class services_user extends AppCompatActivity {
    ImageView back;


TextView description,sub_serviceName,price,myCart;
    Button btn_quantity_1;
    private int quantityValue = 1;
    TextView quantity,serviceName;
    Button btn_quantity_2;
    EditText edit_special_instructions;

    Button service_btn;


    RecyclerView subServiceRecycler;
    List<SubService> subServiceList;
    SubServiceAdapter adapter;
    String selectedName = "", selectedPrice = "", selectedDescription = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_services_user);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }

        description=findViewById(R.id.description);
        back=findViewById(R.id.back);
        serviceName=findViewById(R.id.serviceName);
        myCart=findViewById(R.id.myCart);
        price=findViewById(R.id.price);
        sub_serviceName=findViewById(R.id.sub_serviceName);


        btn_quantity_1=findViewById(R.id.btn_quantity_1);
        quantity=findViewById(R.id.quantity);
        btn_quantity_2=findViewById(R.id.btn_quantity_2);
        edit_special_instructions=findViewById(R.id.edit_special_instructions);

        service_btn=findViewById(R.id.service_btn);

        subServiceRecycler = findViewById(R.id.subServiceRecycler);
        subServiceRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        subServiceList = new ArrayList<>();

        String serviceId = getIntent().getStringExtra("id");
        String service_Name = getIntent().getStringExtra("name");
        serviceName.setText(service_Name);
        myCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(services_user.this, View_Cart.class);
                startActivity(i);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        service_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent book=new Intent(services_user.this,Checkout_user.class);
                startActivity(book);
            }
        });

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("All_Sub_Services");
        dbRef.orderByChild("serviceID").equalTo(serviceId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        subServiceList.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            SubService subService = snap.getValue(SubService.class);
                            subServiceList.add(subService);
                        }

                        adapter = new SubServiceAdapter(services_user.this, subServiceList, subService -> {
                            selectedName = subService.getName();
                            selectedPrice = subService.getPrice();
                            selectedDescription = subService.getDescription();

                            description.setText(selectedDescription);
                            sub_serviceName.setText(selectedName);
                            price.setText("PKR "+selectedPrice);

                            // Use variables as needed (e.g., pass to another activity or show in dialog)
                        });

                        subServiceRecycler.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(services_user.this, "Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        quantity.setText(String.valueOf(quantityValue));

        // Decrease button (-)
        btn_quantity_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantityValue > 1) {
                    quantityValue--;
                    quantity.setText(String.valueOf(quantityValue));
                }
            }
        });

        // Increase button (+)
        btn_quantity_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityValue++;
                quantity.setText(String.valueOf(quantityValue));
            }
        });
        service_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedName.isEmpty()) {
                    Toast.makeText(services_user.this, "Please select a category first", Toast.LENGTH_SHORT).show();
                    return;
                }

                String qty = quantity.getText().toString();
                String instructions = edit_special_instructions.getText().toString();

                // Get userId from SharedPreferences
                String userId = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        .getString("id", "");

                if (userId.isEmpty()) {
                    Toast.makeText(services_user.this, "User not logged in", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Prepare cart item
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("All_Cart");
                String cartId = dbRef.push().getKey();

                Cart cartItem = new Cart(
                        cartId,
                        userId,
                        selectedName,
                        selectedPrice,

                        Integer.parseInt(qty),
                        instructions
                );

                dbRef.child(cartId).setValue(cartItem).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(services_user.this, "Added to cart", Toast.LENGTH_SHORT).show();
                        // Optionally navigate:
                        // startActivity(new Intent(services_user.this, Checkout_user.class));
                    } else {
                        Toast.makeText(services_user.this, "Failed to add: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}