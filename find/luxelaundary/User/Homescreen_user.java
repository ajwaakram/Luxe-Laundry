package com.find.luxelaundary.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.find.luxelaundary.About;
import com.find.luxelaundary.Adapter.ServiceAdapter;
import com.find.luxelaundary.Admin.homescreen_admin;
import com.find.luxelaundary.Chat_user;
import com.find.luxelaundary.Favourites_service_user;
import com.find.luxelaundary.Feedback_user;
import com.find.luxelaundary.Login_user;
import com.find.luxelaundary.Model.Service;
import com.find.luxelaundary.Offers_users;
import com.find.luxelaundary.StainDetectionActivity;
import com.find.luxelaundary.View_Cart;
import com.find.luxelaundary.Profile_user;
import com.find.luxelaundary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Homescreen_user extends AppCompatActivity {
    DrawerLayout drawer;
    RecyclerView recyclerView;
    List<Service> serviceList;
    ServiceAdapter adapter;
    TextView user_name;
    ImageView profileimg;
    TextView profile;
    TextView order;
    TextView chat;
    TextView cart;
    TextView about;
    Button logout;
    TextView feedback,hello,email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homescreen_user);
        drawer=findViewById(R.id.drawer);

        hello=findViewById(R.id.hello);
        email=findViewById(R.id.email);
        profileimg=findViewById(R.id.profileimg);
        profile=findViewById(R.id.profile);
        order=findViewById(R.id.order);
        chat=findViewById(R.id.chat);
        cart=findViewById(R.id.cart);
        about=findViewById(R.id.about);
        feedback=findViewById(R.id.feedback);
        logout=findViewById(R.id.logout);

        user_name = findViewById(R.id.user_name);
        recyclerView = findViewById(R.id.recyclerView); // Make sure ID matches
        serviceList = new ArrayList<>();
        adapter = new ServiceAdapter(this, serviceList);
        loadServices();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);

        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });
        user_name.setText(getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("name", ""));
        hello.setText(getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("name", ""));
        email.setText(getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("email", ""));


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit=new Intent(Homescreen_user.this, Profile_user.class);
                startActivity(edit);
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chat=new Intent(Homescreen_user.this, Chat_user.class);
                startActivity(chat);
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart=new Intent(Homescreen_user.this, View_Cart.class);
                startActivity(cart);
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fav=new Intent(Homescreen_user.this, My_Order.class);
                startActivity(fav);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent about=new Intent(Homescreen_user.this, About.class);
                startActivity(about);
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feedback=new Intent(Homescreen_user.this, Feedback_user.class);
                startActivity(feedback);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout=new Intent(Homescreen_user.this, Login_user.class);
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
                        Toast.makeText(Homescreen_user.this, "Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}