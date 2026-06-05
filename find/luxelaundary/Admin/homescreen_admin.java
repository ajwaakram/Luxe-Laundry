package com.find.luxelaundary.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.find.luxelaundary.Adapter.AdminServiceAdapter;
import com.find.luxelaundary.Adapter.MyOrderAdapter;
import com.find.luxelaundary.Chat_user;
import com.find.luxelaundary.Login_user;
import com.find.luxelaundary.Model.Order;
import com.find.luxelaundary.Model.Service;
import com.find.luxelaundary.R;
import com.find.luxelaundary.StainDetectionActivity;
import com.find.luxelaundary.User.Homescreen_user;
import com.find.luxelaundary.User.My_Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class homescreen_admin extends AppCompatActivity {
    DrawerLayout drawer;
    TextView order;

    private MyOrderAdapter adapter2;
    private ArrayList<Order> orderList ;

    private DatabaseReference ordersRef;
    private RecyclerView rvOrders;

    ImageView menu;
    ImageView service_add;
    TextView hello;
    TextView feedback;
    
    TextView user; 
    Button logout;
    RecyclerView recyclerView;
    List<Service> serviceList;
    AdminServiceAdapter adapter;
TextView user_name,email,detect,chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homescreen_admin);

        chat=findViewById(R.id.chat);
        feedback=findViewById(R.id.feedback);
        detect=findViewById(R.id.detect);
        user_name=findViewById(R.id.user_name);
        email=findViewById(R.id.email);
        drawer=findViewById(R.id.drawer);
        recyclerView = findViewById(R.id.recyclerView);
        rvOrders = findViewById(R.id.rvOrders);
         menu=findViewById(R.id.menu);
        hello=findViewById(R.id.hello);
        order=findViewById(R.id.order);
        
        service_add=findViewById(R.id.service_add);
       
        user=findViewById(R.id.user);
        logout=findViewById(R.id.logout);
        user_name.setText(getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("name", ""));
        hello.setText(getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("name", ""));
        email.setText(getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("email", ""));

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chat=new Intent(homescreen_admin.this, Chat_user.class);
                startActivity(chat);
            }
        });
        serviceList = new ArrayList<>();
        adapter = new AdminServiceAdapter(this, serviceList);
        loadServices();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);

        orderList = new ArrayList<>();
        adapter2 = new MyOrderAdapter();
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        rvOrders.setAdapter(adapter2);

        ordersRef = FirebaseDatabase.getInstance().getReference("All_Order");

        loadOrders();

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit=new Intent(homescreen_admin.this, view_customer_admin.class);
                startActivity(edit);
            }
        });
        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detect=new Intent(homescreen_admin.this, StainDetectionActivity.class);
                startActivity(detect);
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detect=new Intent(homescreen_admin.this, ViewFeedback.class);
                startActivity(detect);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout=new Intent(homescreen_admin.this, Login_user.class);
                startActivity(logout);
            }
        });
        service_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout=new Intent(homescreen_admin.this, Add_Services_Admin.class);
                startActivity(logout);
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout=new Intent(homescreen_admin.this, MyAdminOrder.class);
                startActivity(logout);
            }
        });

    }
    private void loadServices() {
        FirebaseDatabase.getInstance().getReference("All_Services")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        serviceList.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Service service = snap.getValue(Service.class);
                            serviceList.add(service);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(homescreen_admin.this, "Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadOrders() {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot orderSnap : snapshot.getChildren()) {
                    Order order = orderSnap.getValue(Order.class);
                    if (order != null && order.getUserId() != null) {
                        orderList.add(order);
                    }
                }
                adapter2.setOrders(orderList,"1");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(homescreen_admin.this, "Failed to load orders.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}